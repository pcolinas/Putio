package pe.ra.utilities;


/*Objeto bus en el que almacenamos la información necesaria*/
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
    

    /* Método para obtener el id de la línea*/
    public String getName(){
    	return name;
    }
    
    /* Método para obtener los minutos */
    public String getSize(){
    	return size;
    }
    
    /* Método para obtener el id de la parada actual */
    public String getSeeds(){
    	return seeds;
    }
    
    public String getPeers(){
    	return peers;
    }
    
    public String getUrl(){
    	return url;
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
    	this.size = size;
    }
    
    /* Método para modificar el id de la próxima parada */
    public void setSeeds(String seeds){
    	this.seeds =  seeds;
    }
    
    public void setPeers(String peers){
    	this.peers =  peers;
    }
    
    public void setUrl(String url){
    	this.url =  url;
    }
    
    
    
    /* Método para realizar la comparación por el atributo minutos */
   /* public int compareTo(Bus bus) {
        int minutes = Integer.parseInt(((Bus) bus).getMin());
 
        return (Integer.parseInt(this.min) - minutes);
    }*/
      
}