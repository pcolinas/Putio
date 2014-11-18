package pe.ra.utilities;


/*Objeto bus en el que almacenamos la información necesaria*/
public class TransElmnt {
	

	public String name;
    public String size;
    public String percent;
    public String status;
    public String downSpeed;
    public String eta;
    public String id;
    public long pos;
    
    /* Constructor */
    public TransElmnt(String name, String size, String percent, String status, String downSpeed, String eta, String id, long pos){
    	this.name = name;
    	this.size = Functions.showSize(size);
    	this.percent = percent;
    	this.status = status;
    	this.downSpeed = Functions.showSize(downSpeed);
    	if(!eta.equals("null"))
    		this.eta = Functions.showTime(eta);
    	this.id = id;
    	this.pos = pos;
    }
    

    /* Método para obtener el id de la línea*/
    public String getName(){
    	return name;
    }
    
    /* Método para obtener los minutos */
    public String getSize(){
    	return size;
    }
    
    /* Método para obtener el id de la parada actual */
    public String getPercent(){
    	return percent;
    }
    
    public String getStatus(){
    	return status;
    }
    
    public String getDownSpeed(){
    	return downSpeed;
    }
    
    public String getETA(){
    	return eta;
    }
    
    public String getId(){
    	return id;
    }
   
    
    /* Método para obtener el id para la ordenación posterior */
    public long getPos(){
    	return pos;
    }
    

    /* Método para modificar los minutos */
    public void setName(String name){
    	this.name = name;
    }
    
    /* Método para modificar el id de parada */
    public void setSize(String size){
    	this.size = Functions.showSize(size);
    }
    
    /* Método para modificar el id de la próxima parada */
    public void setPercent(String percent){
    	this.percent =  percent;
    }
    
    public void setStatus(String status){
    	this.status =  status;
    }
    
    public void setDownSpeed(String downSpeed){
    	this.downSpeed =  Functions.showSize(downSpeed);
    }
    
    public void setETA(String eta){
    	this.eta =  Functions.showTime(eta);
    }
    
    public void setId(String id){
    	this.id =  id;
    }
    
    
    /* Método para realizar la comparación por el atributo minutos */
   /* public int compareTo(Bus bus) {
        int minutes = Integer.parseInt(((Bus) bus).getMin());
 
        return (Integer.parseInt(this.min) - minutes);
    }*/
      
}