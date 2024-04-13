from kivy.app import App
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.button import Button

class RubiksCubeView(BoxLayout):
    def __init__(self, **kwargs):
        super(RubiksCubeView, self).__init__(**kwargs)
        
        # Créer des boutons représentant chaque face du Rubik's Cube
        for face in ['Front', 'Back', 'Left', 'Right', 'Up', 'Down']:
            button = Button(text=face, size_hint=(None, None), size=(100, 100))
            button.bind(on_press=self.on_face_press)
            self.add_widget(button)
    
    def on_face_press(self, instance):
        # Gérer l'événement lorsque l'utilisateur appuie sur une face
        print("Pressed", instance.text, "face")

class RubiksCubeApp(App):
    def build(self):
        return RubiksCubeView()

if __name__ == "__main__":
    RubiksCubeApp().run()
