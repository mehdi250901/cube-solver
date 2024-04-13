from datetime import datetime
import pytz

def execute(requesting_user, deployment_id, deployment_report, deployer_client, automation_client, deploying_user, deployed_project_key, **kwargs):
    """
    Custom hook function.

    :param str requesting_user: identifier of the DSS user requesting the deployment
    :param str deployment_id: id of the deployment in progress
    :param deployment_report: status of the deployment and messages related if any.
                              In case of a pre deployment hook, this parameter will be None
                              In case of a post deployment hook, it will be a dictionary with:
                                - "status" for the deployment status, can be "SUCCESS", "WARNING" or "ERROR"
                                - "messages" for the list of messages related to the deployment, as strings
    :type deployment_report: dict or None
    :param deployer_client: an api client to connect to the deployer node with the credentials of the user running hooks
    :type deployer_client: A :class:`dataikuapi.dssclient.DSSClient`
    :param automation_client: an api client to connect to the automation node with the credentials defined in the infrastructure settings
    :type automation_client: A :class:`dataikuapi.dssclient.DSSClient`
    :param str deploying_user: identifier of the DSS user executing the deployment
    :param str deployed_project_key: key on the automation node of the deployed project

    :returns: The execution status of the hook and a message as string.
        Use `HookResult.success(message)`, `HookResult.warning(message)` or `HookResult.error(message)`
    """
    now_utc = datetime.now(pytz.timezone('UTC'))
    cur_hour_utc = now_utc.hour
    cur_time = now_utc.strftime("%Y-%d-%m %H:%M:%S")
    cur_day = now_utc.weekday()

    if cur_day < 5 and (cur_hour_utc < 18 or cur_hour_utc > 6):
        return HookResult.error(f"Current time is {cur_time} UTC on day {cur_day}, deployment is only allowed between 18:00 & 7:00 UTC on weekdays")

    return HookResult.success(f"Valid timeframe ({cur_time} on day {cur_day})")
