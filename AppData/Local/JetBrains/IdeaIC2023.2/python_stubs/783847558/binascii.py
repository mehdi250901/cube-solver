# encoding: utf-8
# module binascii
# from (built-in)
# by generator 1.147
""" Conversion between binary data and ASCII """
# no imports

# functions

def a2b_base64(*args, **kwargs): # real signature unknown
    """ Decode a line of base64 data. """
    pass

def a2b_hex(*args, **kwargs): # real signature unknown
    """
    Binary data of hexadecimal representation.
    
    hexstr must contain an even number of hex digits (upper or lower case).
    This function is also available as "unhexlify()".
    """
    pass

def a2b_hqx(*args, **kwargs): # real signature unknown
    """ Decode .hqx coding. """
    pass

def a2b_qp(*args, **kwargs): # real signature unknown
    """ Decode a string of qp-encoded data. """
    pass

def a2b_uu(*args, **kwargs): # real signature unknown
    """ Decode a line of uuencoded data. """
    pass

def b2a_base64(*args, **kwargs): # real signature unknown
    """ Base64-code line of data. """
    pass

def b2a_hex(b, *args, **kwargs): # real signature unknown; NOTE: unreliably restored from __doc__ 
    """
    Hexadecimal representation of binary data.
    
      sep
        An optional single character or byte to separate hex bytes.
      bytes_per_sep
        How many bytes between separators.  Positive values count from the
        right, negative values count from the left.
    
    The return value is a bytes object.  This function is also
    available as "hexlify()".
    
    Example:
    >>> binascii.b2a_hex(b'\xb9\x01\xef')
    b'b901ef'
    >>> binascii.hexlify(b'\xb9\x01\xef', ':')
    b'b9:01:ef'
    >>> binascii.b2a_hex(b'\xb9\x01\xef', b'_', 2)
    b'b9_01ef'
    """
    pass

def b2a_hqx(*args, **kwargs): # real signature unknown
    """ Encode .hqx data. """
    pass

def b2a_qp(*args, **kwargs): # real signature unknown
    """
    Encode a string using quoted-printable encoding.
    
    On encoding, when istext is set, newlines are not encoded, and white
    space at end of lines is.  When istext is not set, \r and \n (CR/LF)
    are both encoded.  When quotetabs is set, space and tabs are encoded.
    """
    pass

def b2a_uu(*args, **kwargs): # real signature unknown
    """ Uuencode line of data. """
    pass

def crc32(*args, **kwargs): # real signature unknown
    """ Compute CRC-32 incrementally. """
    pass

def crc_hqx(*args, **kwargs): # real signature unknown
    """ Compute CRC-CCITT incrementally. """
    pass

def hexlify(data): # known case of binascii.hexlify
    """
    Hexadecimal representation of binary data.
    
      sep
        An optional single character or byte to separate hex bytes.
      bytes_per_sep
        How many bytes between separators.  Positive values count from the
        right, negative values count from the left.
    
    The return value is a bytes object.  This function is also
    available as "b2a_hex()".
    """
    return b""

def rlecode_hqx(*args, **kwargs): # real signature unknown
    """ Binhex RLE-code binary data. """
    pass

def rledecode_hqx(*args, **kwargs): # real signature unknown
    """ Decode hexbin RLE-coded string. """
    pass

def unhexlify(hexstr): # known case of binascii.unhexlify
    """
    Binary data of hexadecimal representation.
    
    hexstr must contain an even number of hex digits (upper or lower case).
    """
    return b""

# classes

class Error(ValueError):
    # no doc
    def __init__(self, *args, **kwargs): # real signature unknown
        pass

    __weakref__ = property(lambda self: object(), lambda self, v: None, lambda self: None)  # default
    """list of weak references to the object (if defined)"""



class Incomplete(Exception):
    # no doc
    def __init__(self, *args, **kwargs): # real signature unknown
        pass

    __weakref__ = property(lambda self: object(), lambda self, v: None, lambda self: None)  # default
    """list of weak references to the object (if defined)"""



class __loader__(object):
    """
    Meta path import for built-in modules.
    
        All methods are either class or static methods to avoid the need to
        instantiate the class.
    """
    def create_module(spec): # reliably restored by inspect
        """ Create a built-in module """
        pass

    def exec_module(module): # reliably restored by inspect
        """ Exec a built-in module """
        pass

    @classmethod
    def find_module(cls, *args, **kwargs): # real signature unknown
        """
        Find the built-in module.
        
                If 'path' is ever specified then the search is considered a failure.
        
                This method is deprecated.  Use find_spec() instead.
        """
        pass

    @classmethod
    def find_spec(cls, *args, **kwargs): # real signature unknown
        pass

    @classmethod
    def get_code(cls, *args, **kwargs): # real signature unknown
        """ Return None as built-in modules do not have code objects. """
        pass

    @classmethod
    def get_source(cls, *args, **kwargs): # real signature unknown
        """ Return None as built-in modules do not have source code. """
        pass

    @classmethod
    def is_package(cls, *args, **kwargs): # real signature unknown
        """ Return False as built-in modules are never packages. """
        pass

    @classmethod
    def load_module(cls, *args, **kwargs): # real signature unknown
        """
        Load the specified module into sys.modules and return it.
        
            This method is deprecated.  Use loader.exec_module() instead.
        """
        pass

    def module_repr(module): # reliably restored by inspect
        """
        Return repr for the module.
        
                The method is deprecated.  The import machinery does the job itself.
        """
        pass

    def __init__(self, *args, **kwargs): # real signature unknown
        pass

    __weakref__ = property(lambda self: object(), lambda self, v: None, lambda self: None)  # default
    """list of weak references to the object (if defined)"""


    _ORIGIN = 'built-in'
    __dict__ = None # (!) real value is "mappingproxy({'__module__': '_frozen_importlib', '__doc__': 'Meta path import for built-in modules.\\n\\n    All methods are either class or static methods to avoid the need to\\n    instantiate the class.\\n\\n    ', '_ORIGIN': 'built-in', 'module_repr': <staticmethod(<function BuiltinImporter.module_repr at 0x0000025D8A192320>)>, 'find_spec': <classmethod(<function BuiltinImporter.find_spec at 0x0000025D8A1923B0>)>, 'find_module': <classmethod(<function BuiltinImporter.find_module at 0x0000025D8A192440>)>, 'create_module': <staticmethod(<function BuiltinImporter.create_module at 0x0000025D8A1924D0>)>, 'exec_module': <staticmethod(<function BuiltinImporter.exec_module at 0x0000025D8A192560>)>, 'get_code': <classmethod(<function BuiltinImporter.get_code at 0x0000025D8A192680>)>, 'get_source': <classmethod(<function BuiltinImporter.get_source at 0x0000025D8A1927A0>)>, 'is_package': <classmethod(<function BuiltinImporter.is_package at 0x0000025D8A1928C0>)>, 'load_module': <classmethod(<function _load_module_shim at 0x0000025D8A1917E0>)>, '__dict__': <attribute '__dict__' of 'BuiltinImporter' objects>, '__weakref__': <attribute '__weakref__' of 'BuiltinImporter' objects>})"


# variables with complex values

__spec__ = None # (!) real value is "ModuleSpec(name='binascii', loader=<class '_frozen_importlib.BuiltinImporter'>, origin='built-in')"

