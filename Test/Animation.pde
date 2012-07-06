// Daniel Shiffman
// Hanukkah 2011
// 8 nights of Processing examples
// http://www.shiffman.net

// The animation object

class Animation {
  float x;  // location for Animation
  float y;  // location for Animation

  // The index into the array is a float!
  // This allows us to vary the speed of the animation
  // It will have to be converted to an int before the actual image is displayed
  float index = 0; 
  
  // Speed, this will control both the animations movement
  // as well as how fast it cycles through the images
  float speed;

  // The array of images
  PImage[] images;
  
  Animation(PImage[] images_, float x_, float y_) {
    images = images_;
    x = x_;
    y = y_;
    
    // A random speed
    speed = random(0.02,0.05);
    // Starting at the beginning
    index = 0;

  }

  void display() {
    // We must convert the float index to an int first!
    int imageIndex = int(index);
    image(images[imageIndex], x, y);
  }

  

  void next() {
    // Move the index forward in the animation sequence
    index += speed;
    // If we are at the end, go back to the beginning
    if (index >= images.length) {
      // We could just say index = 0
      // but this is slightly more accurate
      index -= images.length;
    } 
  }
}

