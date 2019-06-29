#!/usr/bin/env python3

import cv2                 # working with, mainly resizing, images
import numpy as np         # dealing with arrays
import os                  # dealing with directories
from random import shuffle # mixing up or currently ordered data that might lead our network astray in training.  
from tqdm import tqdm      # a nice pretty percentage bar for tasks. Thanks to viewer Daniel BA1/4hler for this suggestion
import tensorflow as tf
import tflearn
from tflearn.layers.conv import conv_2d, max_pool_2d
from tflearn.layers.core import input_data, dropout, fully_connected
from tflearn.layers.estimator import regression

TRAIN_DIR = 'chocolate-vanilla/train'
TEST_DIR = 'chocolate-vanilla/test'
IMG_WIDTH = 160
IMG_HEIGHT = 120
LR = 1e-3
MODEL_NAME = 'chocolate-vanilla_{}_{}.model'.format('5conv-1', LR)
TEST=150

def label_img(img):
    word_label = img.split('.')[-3]
    if word_label == 'chocolate': return [1,0]
    else: return [0,1]

def create_train_data():
    training_data = []
    for img in tqdm(os.listdir(TRAIN_DIR)):
        label = label_img(img)
        if label:
            path = os.path.join(TRAIN_DIR,img)
            img = cv2.imread(path,cv2.IMREAD_COLOR)
            img = cv2.resize(img, (IMG_WIDTH,IMG_HEIGHT))
            training_data.append([np.array(img),np.array(label)])
    shuffle(training_data)
    np.save('train_data.npy', training_data)
    return training_data

def process_test_data():
    testing_data = []
    for img in tqdm(os.listdir(TEST_DIR)):
        path = os.path.join(TEST_DIR,img)
        img_num = img.split('.')[0]
        img = cv2.imread(path,cv2.IMREAD_COLOR)
        img = cv2.resize(img, (IMG_WIDTH,IMG_HEIGHT))
        testing_data.append([img, img_num])        
    shuffle(testing_data)
    np.save('test_data.npy', testing_data)
    return testing_data

def make_model():
	tf.reset_default_graph()
	convnet = input_data(shape=[None, IMG_WIDTH, IMG_HEIGHT, 3], name='input')

	convnet = conv_2d(convnet, 32, 5, activation='relu')
	convnet = max_pool_2d(convnet, 5)

	convnet = conv_2d(convnet, 64, 5, activation='relu')
	convnet = max_pool_2d(convnet, 5)

	convnet = conv_2d(convnet, 128, 5, activation='relu')
	convnet = max_pool_2d(convnet, 5)

	convnet = conv_2d(convnet, 64, 5, activation='relu')
	convnet = max_pool_2d(convnet, 5)

	convnet = conv_2d(convnet, 32, 5, activation='relu')
	convnet = max_pool_2d(convnet, 5)

	convnet = fully_connected(convnet, 1024, activation='relu')
	convnet = dropout(convnet, 0.8)

	convnet = fully_connected(convnet, 2, activation='softmax')
	convnet = regression(convnet, optimizer='adam', learning_rate=LR, loss='categorical_crossentropy', name='targets')

	model = tflearn.DNN(convnet, tensorboard_dir='log')

	if os.path.exists('{}.meta'.format(MODEL_NAME)):
		model.load(MODEL_NAME)
		print('model loaded!')
	return model 

if __name__ == '__main__':
	model=make_model()

	for i in range(20):
		train_data = create_train_data()
		train = train_data [: -TEST ]
		test = train_data [ -TEST :]
		X = np.array([i[0] for i in train]).reshape(-1,IMG_WIDTH,IMG_HEIGHT,3)
		Y = np.array([i[1] for i in train])
		test_x = np.array([i[0] for i in test]).reshape(-1,IMG_WIDTH,IMG_HEIGHT,3)
		test_y = np.array([i[1] for i in test])
		model.fit({'input': X}, {'targets': Y}, n_epoch=5, validation_set=({'input': test_x}, {'targets': test_y}), snapshot_step=500, show_metric=True, run_id=MODEL_NAME)
		model.save(MODEL_NAME)
		print('model save!')


