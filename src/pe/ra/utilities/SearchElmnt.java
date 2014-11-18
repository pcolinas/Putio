package pe.ra.utilities;


/*Objeto bus en el que almacenamos la informaci�n necesaria*/
public class SearchElmnt {
	

	public String name;
    public String size;
    public String seeds;
    public String peers;
    public String url;

    public long pos;
    
    /* Constructor */
    public SearchElmnt(String name, String size, String seeds, String peers, String url, long pos){
    	this.name = name;
    	this.size = size;
    	this.seeds = seeds;
    	this.peers = peers;
    	this.url = url;

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
    public String getSeeds(){
    	return seeds;
    }
    
    public String getPeers(){
    	return peers;
    }
    
    public String getUrl(){
    	return url;
    }
     
    
    /* M�todo para obtener el id para la ordenaci�n posterior */
    public long getPos(){
    	return pos;
    }
    

    /* M�todo para modificar los minutos */
    public void setName(String name){
    	this.name = name;
    }
    
    /* M�todo para modificar el id de parada */
    public void setSize(String size){
    	this.size = size;
    }
    
    /* M�todo para modificar el id de la pr�xima parada */
    public void setSeeds(String seeds){
    	this.seeds =  seeds;
    }
    
    public void setPeers(String peers){
    	this.peers =  peers;
    }
    
    public void setUrl(String url){
    	this.url =  url;
    }
    
    
    
    /* M�todo para realizar la comparaci�n por el atributo minutos */
   /* public int compareTo(Bus bus) {
        int minutes = Integer.parseInt(((Bus) bus).getMin());
 
        return (Integer.parseInt(this.min) - minutes);
    }*/
      
}