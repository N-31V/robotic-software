#!/usr/bin/env python3

from cnn_miracle import *
import cv2 as cv
import sys
import argparse

 
def createParser ():
    parser = argparse.ArgumentParser()
    parser.add_argument ('-o', '--old', action='store_true', default=False)
 
    return parser
 
if __name__ == '__main__':
	parser = createParser()
	namespace = parser.parse_args(sys.argv[1:])
	model = make_model()
	if namespace.old:
		test_data = np.load('test_data.npy', allow_pickle=True)		
	else:
		test_data = process_test_data()

	for elem in test_data[:30]:
		img_num = elem[1]
		img_data = elem[0]
		img_np = np.array(elem[0])
		data = img_np.reshape(IMG_WIDTH,IMG_HEIGHT,3)
		model_out = model.predict([data])[0]

		if np.argmax(model_out) == 1: str_label="Isn't miracle"
		else: str_label='Miracle'

		img = elem[0]

		cv.imshow ( str_label , img)
		cv.waitKey (0)
		cv.destroyAllWindows()











