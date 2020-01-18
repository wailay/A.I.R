import os
source1 = "AIR_DATA"
dest11 = "DATA"
files = os.listdir(source1)
import shutil
import numpy as np
for f in files:
    if np.random.rand(1) < 0.2:
        shutil.move(source1 + '/'+ f, dest11 + '/'+ f)