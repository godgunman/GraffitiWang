class Animate{
  float x;
  float y;
  float index = 0;
  float speed;
  PImage[] images;
  
  Animate(PImage[] images_, float x_, float y_) {
    images = images_;
    x = x_;
    y = y_;
    // A random speed
    speed = random(5,10);
    // Starting at the beginning
    index = 0;

  }
  
   void display() {
    // We must convert the float index to an int first!
    int imageIndex = int(index);
    image(images[imageIndex], x, y);
  }
}
