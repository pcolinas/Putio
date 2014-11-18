package pe.ra.utilities;

import android.graphics.drawable.Drawable;

/*Objeto bus en el que almacenamos la informaci�n necesaria*/
public class FileElmnt {
	
	Drawable pic;
	public String name;
    public String size;
    public String id;
    public String parent_id;
    public String content_type;
    Drawable arrow;
    public String screenshot;
    public String ismp4;
    public long pos;
    
    public FileElmnt(){}
    
    /* Constructor */
    public FileElmnt(Drawable pic, String name, String size, String id, String parent_id, String content_type, Drawable arrow, String screenshot, String ismp4, long pos){
    	this.pic = pic;
    	this.name = name;
    	this.size = Functions.showSize(size);
    	this.id = id;
    	this.parent_id = parent_id;
    	this.content_type = content_type;
    	this.arrow = arrow;
    	this.screenshot = screenshot;
    	this.ismp4 = ismp4;
    	this.pos = pos;
    }
    
    public Drawable getPic(){
    	return pic;
    }
    
    /* M�todo para obtener el id de la l�nea*/
    public String getName(){
    	return name;
    }
    
    /* M�todo para obtener los minutos */
    public String getSize(){
    	return size;
    }
    
    /* M�todo para obtener el id de la parada actual */
    public String getId(){
    	return id;
    }
    
    /* M�todo para obtener el id de la pr�xima parada */
    public String getParentId(){
    	return parent_id;
    }
    
    /* M�todo para obtener el id del trayecto */
    public String getContentType(){
    	return content_type;
    }
    
    public Drawable getArrow(){
    	return arrow;
    }
    
    public String getScreenshot(){
    	return screenshot;
    }
    
    public String getIsMp4(){
    	return ismp4;
    }
    
    
    /* M�todo para obtener el id para la ordenaci�n posterior */
    public long getPos(){
    	return pos;
    }
    
    public void setPic(Drawable pic){
    	this.pic = pic;
    }
    
    /* M�todo para modificar los minutos */
    public void setName(String name){
    	this.name = name;
    }
    
    /* M�todo para modificar el id de parada */
    public void setSize(String size){
    	this.size = Functions.showSize(size);
    }
    
    /* M�todo para modificar el id de la pr�xima parada */
    public void setId(String id){
    	this.id =  id;
    }
    
    /* M�todo para modificar el nombre de la pr�xima parada */
    public void setParentId(String parent_id){
    	this.parent_id =  parent_id;
    }
    
    /* M�todo para modificar el id del trayecto */
    public void setContentType(String content_type){
    	this.content_type =  content_type;
    }
    
    public void setArrow(Drawable arrow){
    	this.arrow = arrow;
    }
    
    public void setScreenshot(String screenshot){
    	this.screenshot =  screenshot;
    }
    
    public void setIsMp4(String ismp4){
    	this.ismp4 =  ismp4;
    }
    
    /* M�todo para realizar la comparaci�n por el atributo minutos */
   /* public int compareTo(Bus bus) {
        int minutes = Integer.parseInt(((Bus) bus).getMin());
 
        return (Integer.parseInt(this.min) - minutes);
    }*/
      
}