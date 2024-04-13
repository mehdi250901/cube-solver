import tensorflow as tf


def _list_physical_devices_tf2():
    return tf.config.list_physical_devices('GPU')


def _list_physical_devices_tf1():
    return tf.config.experimental.list_physical_devices('GPU')
