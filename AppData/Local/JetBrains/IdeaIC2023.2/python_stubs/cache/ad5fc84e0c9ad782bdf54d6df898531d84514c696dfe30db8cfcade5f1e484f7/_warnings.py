# encoding: utf-8
# module _warnings
# from (built-in)
# by generator 1.147
"""
_warnings provides basic warning filtering support.
It is a helper module to speed up interpreter start-up.
"""
# no imports

# Variables with simple values

_defaultaction = 'default'

# functions

def warn(*args, **kwargs): # real signature unknown
    """ Issue a warning, or maybe ignore it or raise an exception. """
    pass

def warn_explicit(*args, **kwargs): # real signature unknown
    """ Low-level interface to warnings functionality. """
    pass

def _filters_mutated(*args, **kwargs): # real signature unknown
    pass

# classes

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
    __dict__ = None # (!) real value is "mappingproxy({'__module__': '_frozen_importlib', '__doc__': 'Meta path import for built-in modules.\\n\\n    All methods are either class or static methods to avoid the need to\\n    instantiate the class.\\n\\n    ', '_ORIGIN': 'built-in', 'module_repr': <staticmethod(<function BuiltinImporter.module_repr at 0x000001FEE7AD2320>)>, 'find_spec': <classmethod(<function BuiltinImporter.find_spec at 0x000001FEE7AD23B0>)>, 'find_module': <classmethod(<function BuiltinImporter.find_module at 0x000001FEE7AD2440>)>, 'create_module': <staticmethod(<function BuiltinImporter.create_module at 0x000001FEE7AD24D0>)>, 'exec_module': <staticmethod(<function BuiltinImporter.exec_module at 0x000001FEE7AD2560>)>, 'get_code': <classmethod(<function BuiltinImporter.get_code at 0x000001FEE7AD2680>)>, 'get_source': <classmethod(<function BuiltinImporter.get_source at 0x000001FEE7AD27A0>)>, 'is_package': <classmethod(<function BuiltinImporter.is_package at 0x000001FEE7AD28C0>)>, 'load_module': <classmethod(<function _load_module_shim at 0x000001FEE7AD17E0>)>, '__dict__': <attribute '__dict__' of 'BuiltinImporter' objects>, '__weakref__': <attribute '__weakref__' of 'BuiltinImporter' objects>})"


# variables with complex values

filters = [
    (
        'default',
        None,
        DeprecationWarning,
        '__main__',
        0,
    ),
    (
        'ignore',
        None,
        '<value is a self-reference, replaced by this string>',
        None,
        0,
    ),
    (
        'ignore',
        None,
        PendingDeprecationWarning,
        None,
        0,
    ),
    (
        'ignore',
        None,
        ImportWarning,
        None,
        0,
    ),
    (
        'ignore',
        None,
        ResourceWarning,
        None,
        0,
    ),
]

_onceregistry = {}

__spec__ = None # (!) real value is "ModuleSpec(name='_warnings', loader=<class '_frozen_importlib.BuiltinImporter'>, origin='built-in')"

