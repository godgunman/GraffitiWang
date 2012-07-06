import processing.net.*;

int port = 9999;

PImage bg;
PImage icon;
int pattern = -2;
float pos_x = 0.0;
float pos_y = 0.0;
int diameter = 20;

float[][] iconPos = new float[10][3];
int cur = 0;
Server myServer;
Client thisClient;


void setup()
{
  size(600, 450);
  textFont(createFont("SanSerif", 16));
  myServer = new Server(this, port); // Starts a myServer on port 9999
  thisClient = myServer.available();
  smooth();
  frameRate(30);
  bg = loadImage("graffiti.jpeg");
}


void pointer(){
  fill(#61AF77);
  noStroke();
  translate(width/2,height/2);
 // text(pos_x, -200,-100);
 // text(pos_y, -100,-100);
  ellipse( pos_x,pos_y, diameter, diameter );
  
  if(icon!=null){
  for( int i =0 ; i< iconPos.length; i++  ){ 
     tint(255, 255-iconPos[i][2] );
     image(icon, iconPos[i][0], iconPos[i][1] );
     iconPos[i][2]+=5;
  }
  }
}

void Parsing( String stream){
  
    try{
      stream = stream.substring(0, stream.indexOf("\n")); 
      String[] data = split(stream,',');
      if(data.length == 4 ){
        pattern = int(data[0]);
        if( pattern == -2 ){
          pos_x = float(data[1]);
          pos_y = -float(data[2]);
          pos_x = map(pos_x, -100, 100, -width/2, width/2 );
          pos_y = map(pos_y, -100, 100, -height/2, height/2 );
        }
        else
          drawEvent();
      }
    }
    catch( Exception  e){
      
    }
    finally{
      println( pos_x+ "," +pos_y );
    }
    
    
}

void draw()
{
    background(bg);
    if( thisClient == null)
      thisClient = myServer.available();
    
    if (thisClient != null) {
      if (thisClient.available() > 0) {
        Parsing( thisClient.readString()  );
      } 
    }
    beginAnimation();
    pointer();
}

void chooseIcon(){
    switch( pattern ){
      case 0:
        icon = loadImage("apple.png");
        break;
      case 1:
         icon = loadImage("book.png");
         break;
      case 2:
         icon = loadImage("android.png");
         break;
      case 3:
           icon = loadImage("camera.png");
           break;
      case 4:
           icon = loadImage("block.png");
           break;
      default:
           break;
    }
}

void drawEvent(){
    chooseIcon();
    println("EV");
    if( cur == 9 )
    cur = 0;
    
    iconPos[cur][0] = pos_x;
    iconPos[cur][1] = pos_y;
    iconPos[cur][2] = 0;
    cur++;
    pattern = -2;
}

void beginAnimation(){
  PImage[] seq = new PImage[5];
  for (int i = 0; i < seq.length; i++) {
    seq[i] = loadImage("animation"+i+".png"); 
  }
  Animate man =new Animate(seq,100,50);
  background(255);
  
  
   man.display();
}
