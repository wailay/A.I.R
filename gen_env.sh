echo "Generating env folder"
python -m venv ./env

echo "Sourcing env folder"

source ./env/bin/activate

echo "Installing packages"

pip install torch torchvision