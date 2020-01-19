import base64
from PIL import Image
from io import BytesIO

def base64toimage(data):
    img = Image.open(BytesIO(base64.b64decode(data)))
    return img

