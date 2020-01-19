import torch, torchvision
from torchvision import datasets, models, transforms
import torch.nn as nn
import torch.optim as optim
from torch.utils.data import DataLoader
import time
from torchsummary import summary

import numpy as np
import matplotlib.pyplot as plt
import os
from os import listdir

from PIL import Image

import pickle


def predict_helper(model, idx_to_class, test_image):
    '''
    Function to predict the class of a single test image
    Parameters
        :param model: Model to test
        :param test_image_name: Test image

    '''
     #Applying Transforms to the Data
    image_transforms = { 
        'test': transforms.Compose([
            transforms.Resize(size=256),
            transforms.CenterCrop(size=224),
            transforms.ToTensor(),
            transforms.Normalize([0.485, 0.456, 0.406],
                                [0.229, 0.224, 0.225])
        ])
    }
    transform = image_transforms['test']

    
    plt.imshow(test_image)
    
    test_image_tensor = transform(test_image)

    if torch.cuda.is_available():
        test_image_tensor = test_image_tensor.view(1, 3, 224, 224).cuda()
    else:
        test_image_tensor = test_image_tensor.view(1, 3, 224, 224)
    
    with torch.no_grad():
        model.eval()
        # Model outputs log probabilities
        out = model(test_image_tensor)
        nb_prediction = 1
        ps = torch.exp(out)
        topk, topclass = ps.topk(nb_prediction, dim=1)
        return idx_to_class[topclass.cpu().numpy()[0][0]]

def predict(image):
    
    model = torch.load('utils/DATA_model_9.pt')

    
    with open('utils/idx_to_class.pickle', 'rb') as handle:
        idx_to_class = pickle.load(handle)

    return predict_helper(model, idx_to_class, image)