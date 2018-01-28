# When running, set the variable VK_CODE ($1)

# Downloading photos from VK
echo "Running VK Photo Downloader"
java -jar vk-photo-downloader-0.1.0-SNAPSHOT-jar-with-dependencies.jar "$1" "$2"

# Parsing photo and running open_nsfw
echo "Running open-nsfw"
echo "Results:" > results.txt
OUTPUT="$(docker run -a stdin -a stdout --volume=$(pwd):/workspace bvlc/caffe:cpu \
        python ./classify_dir.py \
        --model_def nsfw_model/deploy.prototxt \
        --pretrained_model nsfw_model/resnet_50_1by2_nsfw.caffemodel \
        photo \
        | grep NSFW \
        | awk '{ print $3 " " $4 }'
        )"
echo ${OUTPUT} >> results.txt

# Sorting results
echo "Running sorter"
java -jar $(pwd)/sorter-0.1.0-SNAPSHOT-jar-with-dependencies.jar "results.txt" "sortedResults.txt"