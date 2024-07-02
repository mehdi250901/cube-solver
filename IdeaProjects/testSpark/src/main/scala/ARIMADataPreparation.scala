import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._
import org.jfree.chart.axis.DateAxis
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import org.jfree.chart.{ChartFactory, ChartPanel, JFreeChart}
import org.jfree.data.xy.{XYSeries, XYSeriesCollection}
import org.jfree.ui.{ApplicationFrame, RefineryUtilities}

import java.awt.geom.Ellipse2D
import java.awt.{Color, Shape}
import java.text.SimpleDateFormat

object StationaryDataVisualization {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("StationaryDataVisualization")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    val filePath = "csvInter/nan_interpolated_signals"
    val df = spark.read.option("header", "true").option("inferSchema", "true").csv(filePath)

    val dfWithTimestamp = df.withColumn("timestamp", unix_timestamp(col("_c0"), "yyyy-MM-dd HH:mm:ss").cast("timestamp"))
    val windowSpec = Window.orderBy("timestamp")

    // Statistiques avant différenciation
    df.describe("signal_std_interpolated", "signal_rad_interpolated").show()

    // Rendre les données stationnaires par différenciation
    val dfDifferenced = dfWithTimestamp.withColumn("signal_std_diff", col("signal_std_interpolated") - lag("signal_std_interpolated", 1).over(windowSpec))
      .withColumn("signal_rad_diff", col("signal_rad_interpolated") - lag("signal_rad_interpolated", 1).over(windowSpec))
      .filter(col("signal_std_diff").isNotNull && col("signal_rad_diff").isNotNull)

    // Statistiques après différenciation
    dfDifferenced.describe("signal_std_diff", "signal_rad_diff").show()

    // Collecter les résultats pour le graphique
    val allRowsList = dfDifferenced.collect()

    // Créer les séries pour JFreeChart
    val seriesSignalStdDiff = new XYSeries("signal_std_diff")
    val seriesSignalRadDiff = new XYSeries("signal_rad_diff")
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    allRowsList.foreach { row =>
      val timestamp = row.getAs[java.sql.Timestamp]("timestamp").getTime.toDouble
      seriesSignalStdDiff.add(timestamp, row.getAs[Double]("signal_std_diff"))
      seriesSignalRadDiff.add(timestamp, row.getAs[Double]("signal_rad_diff"))
    }

    // Créer le dataset
    val dataset = new XYSeriesCollection()
    dataset.addSeries(seriesSignalStdDiff)
    dataset.addSeries(seriesSignalRadDiff)

    // Créer le graphique
    val chart: JFreeChart = ChartFactory.createTimeSeriesChart(
      "Valeurs des signaux après différenciation",
      "Date",
      "Valeur du signal",
      dataset
    )

    // Configurer les couleurs des séries
    val plot = chart.getXYPlot
    val renderer = new XYLineAndShapeRenderer(true, false) // true pour dessiner les lignes

    // Définir des petits points ronds pour les valeurs différenciées
    val shape: Shape = new Ellipse2D.Double(-1.0, -1.0, 2.0, 2.0)
    renderer.setSeriesShape(0, shape)
    renderer.setSeriesShape(1, shape)

    renderer.setSeriesPaint(0, Color.GREEN) // signal_std_diff en vert
    renderer.setSeriesPaint(1, Color.BLUE)  // signal_rad_diff en bleu
    plot.setRenderer(renderer)

    // Configurer l'axe des x pour afficher les dates
    val dateAxis = new DateAxis("Date")
    dateAxis.setDateFormatOverride(new SimpleDateFormat("MMM yyyy"))
    plot.setDomainAxis(dateAxis)

    // Afficher le graphique dans une fenêtre
    val chartFrame = new ApplicationFrame("Graphique des Signaux après Différenciation")
    val chartPanel = new ChartPanel(chart)
    chartPanel.setPreferredSize(new java.awt.Dimension(800, 600))
    chartFrame.setContentPane(chartPanel)
    chartFrame.pack()
    RefineryUtilities.centerFrameOnScreen(chartFrame)
    chartFrame.setVisible(true)

    // Sauvegarder les résultats dans un fichier CSV
    dfDifferenced.select("_c0", "signal_std_diff", "signal_rad_diff", "pluie")
      .write.option("header", "true").csv("csvInter/differenced_signals")

    // Fermer la session Spark
    spark.stop()
  }
}
