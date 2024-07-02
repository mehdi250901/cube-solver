import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._

object _06_StationarityTransformation {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("StationarityTransformation")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    // Charger les données depuis le fichier CSV
    val filePath = "csvInter/nan_interpolated_signals"
    val df = spark.read.option("header", "true").option("inferSchema", "true").csv(filePath)

    // Ajouter une colonne timestamp
    val dfWithTimestamp = df.withColumn("timestamp", unix_timestamp(col("_c0"), "yyyy-MM-dd HH:mm:ss").cast("timestamp"))

    // Créer une fenêtre pour les opérations de décalage
    val windowSpec = Window.orderBy("timestamp")

    // Étape 1: Transformation logarithmique
    val dfLogTransformed = dfWithTimestamp.withColumn("log_signal_std", log(col("signal_std_interpolated")))
      .withColumn("log_signal_rad", log(col("signal_rad_interpolated")))

    // Étape 2: Différenciation de premier ordre
    val dfDifferenced = dfLogTransformed
      .withColumn("log_signal_std_diff", col("log_signal_std") - lag("log_signal_std", 1).over(windowSpec))
      .withColumn("log_signal_rad_diff", col("log_signal_rad") - lag("log_signal_rad", 1).over(windowSpec))
      .filter(col("log_signal_std_diff").isNotNull && col("log_signal_rad_diff").isNotNull)

    // Afficher les résultats
    dfDifferenced.select("timestamp", "log_signal_std_diff", "log_signal_rad_diff").show()

    // Sauvegarder les résultats dans un fichier CSV
    dfDifferenced.select("_c0", "log_signal_std_diff", "log_signal_rad_diff", "pluie")
      .write.option("header", "true").csv("csvInter/differenced_signals")

    // Fermer la session Spark
    spark.stop()
  }
}
