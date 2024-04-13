import os, json, requests, time, traceback, boto3
from datetime import datetime

imds_token = requests.put("http://169.254.169.254/latest/api/token", headers = {'X-aws-ec2-metadata-token-ttl-seconds': '2160'}).text
imds_header = { 'X-aws-ec2-metadata-token': imds_token}
instance_info = requests.get("http://169.254.169.254/latest/dynamic/instance-identity/document", headers = imds_header).content
region = json.loads(instance_info)['region']
instance_id = requests.get("http://169.254.169.254/latest/meta-data/instance-id", headers = imds_header).text

client = boto3.client('cloudwatch', region)
pod_id = os.environ.get("POD_ID", None)
pod_name = os.environ.get("POD_NAME", None)
namespace_name = os.environ.get("NAMESPACE_NAME", None)
metrics_url  = os.environ.get("METRICS_URL")

dimensions = []
if pod_id is not None:
    dimensions.append({"Name":"PodId", "Value":pod_id})
if pod_name is not None:
    dimensions.append({"Name":"PodName", "Value":pod_name})
if instance_id is not None:
    dimensions.append({"Name":"InstanceId", "Value":instance_id})
if namespace_name is not None:
    dimensions.append({"Name":"NamespaceName", "Value":namespace_name})

def serve_one():
    r = requests.get(metrics_url)
    metrics_json = r.content
    print("Got %s" % metrics_json)
    metric_data = []
    for k, v in json.loads(metrics_json).items():
        metric_data.append({"MetricName":k, "Timestamp":datetime.now(), "Value":v, "Dimensions": dimensions})
    client.put_metric_data(Namespace="DKU/Webapps", MetricData=metric_data)
    
def serve():
    print("------- start forwarding")
    while True:
        time.sleep(30)
        try:
            serve_one()
        except:
            traceback.print_exc()
            print("Failed to send metrics")

if __name__ == "__main__":
    serve()
