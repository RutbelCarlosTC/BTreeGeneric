public class BTree <T extends Comparable<T>> {
	BNodeGeneric<T> root; // Pointer to root node
    int t;  // Minimum degree
    public BTree(int t){
    	root = null;  
    	this.t = t; 
    }
 
    // function to traverse the tree
    public void traverse() {
    	
    	if (root != null) 
    		root.traverse(); 
    }
    
    // function to search a key in this tree
    BNodeGeneric<T> search(T k) {
        return (root == null)? null : root.search(k);
    }
	//The main function that inserts a new key in this B-Tree
	public void insert(T k){
	
		if (root == null) {
			
			root = new BNodeGeneric<T>(t, true);
			root.keys.set(0,k); 
			root.n = 1; 
		}
		else { 
		
			// Si la raíz está llena, entonces el árbol crece en altura
			if (root.n == 2*t-1) {
			
				// Asignar memoria para nueva raíz
				BNodeGeneric<T> s = new BNodeGeneric<T>(t, false);

				// Hacer vieja raíz como hija de nueva raíz
				s.C.set(0, root);

				// Dividir la raíz antigua y mover 1 clave a la raíz nueva
				s.splitChild(0, root);

				// La nueva raiz tiene dos hijos ahora. Decide cuál 
				//de los dos hijos va a tener clave nueva
				int i = 0;
				if (s.keys.get(0).compareTo(k) < 0)
					i++;
				s.C.get(i).insertNonFull(k);

				// Change root
				root = s;
			}
			else // If root is not full, call insertNonFull for root
				root.insertNonFull(k);
		}
	}
	
	public void remove(T k) {
	    if (root == null){
	        System.out.println("El arbol esta vacio\n");
	        return;
	    }
	 
	   //llamar a la funcion remove para root
	    root.remove(k);
	 
	   // Si el nodo raíz tiene 0 claves, haga que su primer hijo
	   //sea la nueva raíz si tiene un hijo, de lo contrario establezca la raíz como NULL
	    if (root.n==0) {
	        if (root.leaf)
	            root = null;
	        else
	            root = root.C.get(0);
	    }
	    return;
	}
}