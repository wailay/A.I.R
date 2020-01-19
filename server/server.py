from flask import Flask, request, jsonify
from flask_restful import Resource, Api,reqparse
from utils.base64toimage import base64toimage
from utils.predict import predict
import pymongo
from PIL import Image
from bson import json_util
import json
import os
import base64
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
        img = base64toimage(data['image'])
        #prediction here
        
        prediction = predict(img)
        print(prediction)
        return {"result" : prediction}

class GetAllTrash(Resource):
    def get(self):
        
        allBins = bins.find({})    
        a = json.loads(json_util.dumps(allBins))
        return {"result" : a}

class GetTrashPicture(Resource):
    def get(self, trash_id):
        print("ALOOOOO ", type(trash_id))
        path = f'./bin_images/{trash_id}.png'
        with open(path, "rb") as image_file:
            encoded_string = base64.b64encode(image_file.read()).decode()
        # print(encoded_string)
        return encoded_string
class AddTrash(Resource):
    def post(self):

        data = request.get_json(force=True)
        img = data['image']
        longitude = data['long']
        lat = data['lat']
        pass

api.add_resource(ImageAnalyser, '/image')
api.add_resource(GetAllTrash, '/trash')
api.add_resource(AddTrash, '/trash')
api.add_resource(GetTrashPicture, '/trash/<string:trash_id>')



if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0")