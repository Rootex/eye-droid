'''
Kivy application responsible for fetching all pictures in the image folder,
adding a border around them and displaying them to the screen. Also with the
capability of rotating images in both directions and enlarging.
'''

import kivy
kivy.require('1.0.5')

from glob import glob
from random import randint
from os.path import join, dirname
from kivy.app import App
from kivy.logger import Logger
from kivy.uix.scatter import Scatter
from kivy.properties import StringProperty
from kivy.core.window import Window


class Frame(Scatter):
    '''Picture is the class that will show the image with a white border and a
    shadow. They are nothing here because almost everything is inside the
    picture.kv. Check the rule named <Picture> inside the file, and you'll see
    how the Picture() is really constructed and used.

    The source property will be the filename to show.
    '''

    source = StringProperty(None)


class ImagesApp(App):

    def build(self):

        # the root is created in pictures.kv
        root = self.root

        # get any files into images directory
        curdir = dirname(__file__)
        for filename in glob(join(curdir, 'Images', '*')):
            try:
                # load the image
                picture = Frame(source=filename, rotation=randint(-30,30))
                # add to the main field
                root.add_widget(picture)
            except Exception, e:
                Logger.exception('Images: Unable to load <%s> <%s> ' % (filename, e))

    def on_pause(self):
        return True


if __name__ in ('__main__', '__android__'):
    ImagesApp().run()

