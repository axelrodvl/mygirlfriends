# Parsing photo and running open_nsfw
echo "Invoking open_nsfw"
echo "Results:" > results.txt

echo "Copying modified classify script to open_nsfw"
cp classify_dir.py open_nsfw

echo "Changing directory to open_nsfw"
cd open_nsfw
echo $(pwd)

echo "Running open_nsfw"
OUTPUT="$(docker run -a stdin -a stdout --volume=$(pwd):/workspace bvlc/caffe:cpu \
        python ./classify_dir.py \
        --model_def nsfw_model/deploy.prototxt \
        --pretrained_model nsfw_model/resnet_50_1by2_nsfw.caffemodel \
        ../photo \
        | grep NSFW \
        | awk '{ print $3 " " $4 }'
        )"
cd ..

echo ${OUTPUT} >> results.txt