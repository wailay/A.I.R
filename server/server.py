from flask import Flask, request
from flask_restful import Resource, Api,reqparse
from utils.base64toimage import base64toimage
from utils.predict import predict
<<<<<<< HEAD
import numpy as np
=======
import pymongo
>>>>>>> 37eb99cbe42ef62e2811a4f0dd816cfb583d8349

app = Flask(__name__)
api = Api(app)
mongo = pymongo.MongoClient("mongodb://localhost:27017/")
db = mongo['trash_db']
bins = db['bins']

class ImageAnalyser(Resource):
    def get(self):
        return "Hello"

    def post(self):
        data = request.get_json(force=True)

        #get image
        img = base64toimage(data['image'])

        # #get contribution flag
        # contribute = data['contribution']

        # #save image if contributed
        # if contribue == 1:
        #     image_class = data['class']
        #     #saves image to to corresponding class folder with a random name
        #     img.save(image_class + '/' + np.random.randint(low=1, high=1000000000, size=1) + '.jpg')


        #prediction here
        prediction = predict(img)
        print(prediction)
        return prediction

class TrashGeo(Resource):
    def get(self):
        # bins.insert({})

api.add_resource(ImageAnalyser, '/image')
if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0")