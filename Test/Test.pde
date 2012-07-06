import processing.net.*;

int port = 9999;

PImage bg;
PImage[]icon = new PImage[4];
int pattern = -2;
float pos_x = 0.0;
float pos_y = 0.0;
int diameter = 20;
boolean start = false;
int count = 0;


float[][] iconPos = new float[10][3];
PImage[] icns = new PImage[10];
int cur = 0;
Server myServer;
Client thisClient;
Animation man;

void setup()
{
  size(600, 450);
  textFont(createFont("SanSerif", 16));
  myServer = new Server(this, port); // Starts a myServer on port 9999
  thisClient = myServer.available();
  smooth();
  frameRate(30);
  bg = loadImage("graffiti.jpeg");
  
  PImage[] seq = new PImage[5];
  for (int i = 0; i < seq.length; i++) {
    seq[i] = loadImage("animation"+(i+1)+".png"); 
    seq[i].resize(230,307);
  }
  
  man = new Animation(seq,40,120); 
  
  IconSet();
}


void pointer(){
  fill(#61AF77);
  noStroke();
  translate(width/2,height/2);
 // text(pos_x, -200,-100);
 // text(pos_y, -100,-100);
  ellipse( pos_x,pos_y, diameter, diameter );
  
  if( haveSomething() ){
    for( int i =0 ; i< iconPos.length; i++  ){ 
       tint(255, 255-iconPos[i][2] );
       if( icns[i] != null ){
         image( icns[i], iconPos[i][0], iconPos[i][1] );
         iconPos[i][2]+=5;
         if( iconPos[i][2] > 255 ){
              count--;
              iconPos[i][2] = 0;
         }
       }
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
          pos_x = -float(data[1]);
          pos_y = -float(data[2]);
          pos_x = map(pos_x, -100, 100, -width/2, width/2 );
          pos_y = map(pos_y, -100, 100, -height/2, height/2 );
        }
        else{
          drawEvent();
          
        }
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
    PImage pimg;
    if( thisClient == null)
      thisClient = myServer.available();
    
    if (thisClient != null) {
      if (thisClient.available() > 0) {
        start = true;
        Parsing( thisClient.readString()  );
      } 
    }
    if(!start)
      beginAnimation();
    else if(start)
      pointer( );
   
}

void IconSet(){
    imageMode(CENTER);
    icon[0] = loadImage("imgres-shake.png");
    icon[1] = loadImage("throw.png");
    icon[2] = loadImage("tap.png");
    icon[3] = loadImage("circle.png");
     
}

void drawEvent(){
    
    println("EV");
    if( cur == 9 )
    cur = 0;
    iconPos[cur][0] = pos_x;
    iconPos[cur][1] = pos_y;
    iconPos[cur][2] = 0;
    icns[cur] = icon[pattern];
    
    cur++;
    count++;
   
   
}


boolean haveSomething(){
   if( count > 0)
     return true;
    else
      return false;
}

void beginAnimation(){
  background(255);
  
  man.display();
  man.next();
  

}
