from flask import Flask, request
from flask_restful import Resource, Api,reqparse
from utils.base64toimage import base64toimage
from utils.predict import predict

app = Flask(__name__)
api = Api(app)
class ImageAnalyser(Resource):
    def get(self):
        return "Hello"

    def post(self):
        data = request.get_json(force=True)
        img = base64toimage(data['image'])
        #prediction here
        
        prediction = predict(img)
        print(prediction)
        return prediction


api.add_resource(ImageAnalyser, '/image')
if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0")