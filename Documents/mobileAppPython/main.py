import kivy 
from kivy.app import App
from kivy.uix.boxlayout import BoxLayout

kivy.require('2.0.0')

class GameView(BoxLayout):
    def __init__(self,  **kwargs):
        super(GameView, self).__init__( **kwargs)
    
    
class MehdiApp(App):
    def build(self):
        return GameView()
    
mehdiApp = MehdiApp()

if __name__ == '__main__':
    mehdiApp.run()

    