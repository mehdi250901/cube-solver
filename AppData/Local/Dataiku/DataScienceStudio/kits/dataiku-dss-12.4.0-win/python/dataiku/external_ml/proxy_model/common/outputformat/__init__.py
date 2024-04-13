import logging

import pandas as pd

from .base_reader import BaseReader
from .. import BaseWriter

logger = logging.getLogger(__name__)


def guess_input_and_output_formats(output_formats_to_try, prediction_type, input_df, input_formats_and_predictions, value_to_class):
    """

    :param output_formats_to_try: 
    :type output_formats_to_try: list[type]
    :param prediction_type: 
    :param input_df: 
    :type input_df: pd.DataFrame
    :param input_formats_and_predictions: 
    :type input_formats_and_predictions: list[(BaseWriter, Any)]
    :param value_to_class: 
    :rtype: (BaseWriter, BaseReader)
    """
    for input_format, predictions in input_formats_and_predictions:
        output_format = _guess_output_format(output_formats_to_try, prediction_type, input_df, predictions, value_to_class)
        if output_format is not None:
            return input_format, output_format

    err = "The input/output formats are not compatible with the endpoint or the endpoint is not healthy."
    raise ValueError(err)


def guess_input_format_from_output_format(output_format, input_df, input_formats_and_predictions):
    """

    :param output_format: 
    :type output_format: BaseReader
    :param input_df: 
    :type input_df: pd.DataFrame
    :param input_formats_and_predictions: 
    :type input_formats_and_predictions: list[(BaseWriter, Any)]
    :rtype: BaseWriter
    """
    for input_format, predictions in input_formats_and_predictions:
        if _is_output_format_valid(output_format, input_df, predictions):
            return input_format

    err = "The input formats are not compatible with the output format or the endpoint. Or the endpoint is not healthy."
    raise ValueError(err)


def _guess_output_format(formats_to_try, prediction_type, input_df, predictions, value_to_class):
    """

    :param formats_to_try: 
    :type formats_to_try: list[type]
    :param prediction_type: 
    :param input_df: 
    :type input_df: pd.DataFrame
    :param predictions: 
    :param value_to_class: 
    :rtype: Optional[BaseReader]
    """
    for format_to_try in formats_to_try:
        f = format_to_try(prediction_type, value_to_class)
        if _is_output_format_valid(f, input_df, predictions):
            return f

    return None


def _is_output_format_valid(f, input_df, predictions):
    """

    :param f: 
    :type f: BaseReader
    :param input_df: 
    :type input_df: pd.DataFrame
    :param predictions: 
    :rtype: bool
    """
    try:
        expected_len = len(input_df)
        if f.can_read(predictions):
            actual_len = len(f.read(predictions))
            if expected_len == actual_len:
                return True
            else:
                logger.info("Discarding output format {} because input row count {} does not match output row count {}".format(f.NAME, expected_len, actual_len))
                return False
    except Exception as e:
        message = "Exception while trying format: {}. Exception message: {}".format(f.NAME, e)
        logger.info(message)

    return False
