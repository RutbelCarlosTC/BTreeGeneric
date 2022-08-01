import java.util.Vector;

public class BNodeGeneric<T extends Comparable<T>> {
	Vector <T>keys; // Una matriz de keys
	int t;	 // Grado mínimo (define el rango para el número de claves)
	Vector<BNodeGeneric<T>> C; // Una matriz de punteros secundarios
	int n;	 // Número actual de claves
	boolean leaf;//es hoja o no
	public BNodeGeneric (int t1, boolean leaf1) {
		t = t1;
		leaf = leaf1;
		keys = new Vector<T>(2*t-1); //max de claves posibles
		C = new Vector<BNodeGeneric<T>>(2*t);//max de hijos posibles
		for(int i = 0; i < 2*t-1;i++) {
			keys.add(null);
			C.add(null);
		}
		C.add(null);
		
		// incializar el numero actual de claves a 0
		n = 0;
	}

	//Función para atravesar todos los nodos en un subárbol enraizado con este nodo
	public void traverse(){
		// Hay n claves y n+1 hijos, atravesar n claves y los primeros n hijos
		int i;
		for (i = 0; i < n; i++){
			// Si el nodo no es hoja entonces antes de imprimir key[i],
			// Hacemos traverse del subarbol con el hijo C[i].
			if (leaf == false)
				C.get(i).traverse();
			System.out.print(keys.get(i) + " ");// si es hoja imprime todas sus keys(n)
											
		}	
		// Imprime el subárbol enraizado con el último hijo
		if (leaf == false)
			C.get(i).traverse();
	}

	//Función para buscar la clave k en el subárbol enraizado con este nodo
	public BNodeGeneric<T> search(T k) {
		// Encuentra la primera clave mayor o igual a k
		int i = 0;
		while (i < n && k.compareTo(keys.get(i)) > 0)// avanzamos miengras keys[i] sea menor a k
			i++;

		// Si la clave encontrada es igual a k, devuelve este nodo
		if (keys.get(i).equals(k))
			return this;

		// Si la clave no se encuentra aquí y este es un nodo hoja
		if (leaf == true)
			return null;

		// Ir al hijo apropiado
		return C.get(i).search(k);// si es mayor nos vamos a su hijo i menor y repetimos  
	}

	//Una función de utilidad para insertar una nueva clave en este nodo
	//La suposición es que el nodo no debe estar lleno!!! cuando se llama a esta función
	void insertNonFull(T k){
		// Inicializa el índice como índice del elemento más a la derecha
		int i = n-1;

		// Si este es un nodo hoja
		if (leaf == true){
			// El siguiente ciclo hace dos cosas
			// a) Encuentra la ubicación de la nueva clave que se insertará
			// b) Mueve todas las claves mayores a un lugar adelante
			while (i >= 0 && keys.get(i).compareTo(k) > 0){
				keys.set(i+1, keys.get(i));
				i--;
			}

			// Inserta la nueva clave en la ubicación encontrada
			keys.set(i+1, k);
			n = n+1;
		}
		else {// Si este nodo no es hoja
		
			// Encuentra el hijo que va a tener la nueva clave
			while (i >= 0 && keys.get(i).compareTo(k) > 0)
				i--;

			// Ver si el hijo encontrado está lleno
			if (C.get(i+1).n == 2*t-1){
				// Si el niño está lleno, entonces divídalo
				splitChild(i+1, C.get(i+1));

				// Después de dividir, la tecla central de C[i] 
				//sube y C[i] se divide en dos. Mira cuál de 
				//los dos va a tener la nueva llave
				if (keys.get(i+1).compareTo(k) < 0)
					i++;
			}
			C.get(i+1).insertNonFull(k);
		}
	}

	//Una función de utilidad para dividir el hijo y de este nodo
	//Tenga en cuenta que y debe estar lleno cuando se llama a esta función
	void splitChild(int i, BNodeGeneric<T> y){///NODO.splitChild(pos Hijo, ref Hijo)
		// Crea un nuevo nodo que va a almacenar (t-1) claves de y
		BNodeGeneric<T> z = new BNodeGeneric<T>(y.t, y.leaf);
		z.n = t - 1;

		// Copiar las últimas claves (t-1) de y a z
		for (int j = 0; j < t-1; j++)
			z.keys.set(j, y.keys.get(j+t));

		// Copia los últimos t hijos de y a z
		if (y.leaf == false){
			for (int j = 0; j < t; j++)
				z.C.set(j, y.C.get(j+t));
		}

		// Reducir el número de claves en y
		y.n = t - 1; // elimina indirectamente los ultimos elementos de y
						//porque los metodos usan recorrrido con "n" actual

		// Dado que este nodo va a tener un nuevo hijo,
		//crea un espacio para un nuevo hijo(funciona si el nodo no esta lleno)
		for (int j = n; j >= i+1; j--) // n es num de clves actual del NODO "padre" asi que va desde la ultima pos de hijos
			C.set(j+1, C.get(j));

		// Vincular el nuevo hijo a este nodo
		C.set(i+1, z); //(i+j) es la pos sgte del hijo y

		// Una clave de y se moverá a este nodo. 
		//Encuentre la ubicación de la nueva clave y mueva todas las claves mayores un espacio hacia adelante
		for (int j = n-1; j >= i; j--) 
			keys.set(j+1, keys.get(j));

		// Copia la clave del medio de y a este nodo
		keys.set(i, y.keys.get(t-1));

		// Incrementa el conteo de claves en este nodo
		n = n + 1;
	}
	///////
	//////
	////Metodos para eliminar 
	// Una función de utilidad que devuelve el índice de la primera clave que es mayor o igual que k
	int findKey(T k){
		int idx=0;
		while (idx<n && keys.get(idx).compareTo(k) < 0)
			++idx;
		return idx;
	}

	// Una función para eliminar la clave k del subárbol enraizado con este nodo
	void remove(T k){
		int idx = findKey(k);

		// La clave a eliminar está presente en este nodo
		if (idx < n && keys.get(idx).equals(k)){

			// Si el nodo es un nodo hoja, se llama a removeFromLeaf
			// De lo contrario, se llama a la función removeFromNonLeaf
			if (leaf)
				removeFromLeaf(idx);
			else
				removeFromNonLeaf(idx);
		}
		else{

			// Si este nodo es un nodo hoja, entonces la clave no está presente en el árbol
			if (leaf){
				System.out.println("La clave " + k + " no existe en el arbol\n");
				return;
			}

			// La clave que se eliminará está presente en el subárbol enraizado con este nodo
			// La bandera indica si la clave está presente en el subárbol enraizado
			// con el último hijo de este nodo
			boolean flag = (idx==n)? true : false ;

			// Si el hijo donde se supone que existe la clave tiene menos 
			//de t claves, llenamos ese hijo
			if (C.get(idx).n < t)
				fill(idx);

			// Si el último hijo se fusionó, debe haberse fusionado con el hijo anterior, 
			//por lo que recurrimos al (idx-1) hijo. De lo contrario, 
			//recurrimos al (idx)th hijo que ahora tiene al menos t claves
			if (flag && idx > n)
				C.get(idx-1).remove(k);
			else
				C.get(idx).remove(k);
		}
		return;
	}

	// Una función para eliminar la clave idx-th de este nodo, que es un nodo hoja
	void removeFromLeaf (int idx){

		// Mueve todas las teclas después de la posición idx-th un lugar hacia atrás
		for (int i=idx+1; i<n; ++i)
			keys.set(i-1, keys.get(i));
		
		n--;

		return;
	}

	// Una función para eliminar la clave idx-th de este nodo, que es un nodo que no es hoja
	void removeFromNonLeaf(int idx){

		T k = keys.get(idx);

		// Si el hijo que precede a k (C[idx]) tiene al menos t claves, busque el predecesor 'pred' 
		//de k en el subárbol con raíz en C[idx]. 
		//Reemplace k por pred. Eliminar recursivamente pred en C[idx]
        if (C.get(idx).n >= t){
            T pred = getPred(idx);
            keys.set(idx,pred);
            C.get(idx).remove(pred);                                            
        }

		// Si el hijo C[idx] tiene menos de t claves, examine C[idx+1].
		// Si C[idx+1] tiene al menos t claves, busque el sucesor 'succ' 
		//de k en el subárbol enraizado en C[idx+1] Reemplace k por succ
		// Borrar recursivamente succ en C[idx+1]
        else if (C.get(idx+1).n >= t){
            T succ = getSucc(idx);
            keys.set(idx,succ);
            C.get(idx+1).remove(succ);                                          
        }


		// Si tanto C[idx] como C[idx+1] tienen menos de t claves, combine k y todo C[idx+1] en C[idx]
		// Ahora C[idx] contiene claves 2t-1
		// Libere C[idx+1] y elimine recursivamente k de C[idx]
		else{
			merge(idx);
			C.get(idx).remove(k);
		}
		return;
	}

	// Una función para obtener el predecesor de las claves [idx]
	T getPred(int idx){
		// Sigue moviéndote hacia el nodo más a la derecha hasta que lleguemos a una hoja
		BNodeGeneric<T> cur=C.get(idx);
		while (!cur.leaf)
			cur = cur.C.get(cur.n);

		// Devuelve la última clave de la hoja
		return cur.keys.get(cur.n-1);
	}

	T getSucc(int idx){

		// Sigue moviendo el nodo más a la izquierda comenzando desde C[idx+1] hasta llegar a una hoja
		BNodeGeneric<T> cur=C.get(idx+1);
		while (!cur.leaf)
			cur = cur.C.get(0);

		// Devuelve la última clave de la hoja
		return cur.keys.get(0);
		
	}

	// Una función para llenar el niño C[idx] que tiene menos de t-1 claves
	void fill(int idx){

		// Si el hijo anterior (C[idx-1]) tiene más de t-1 claves, toma prestada una clave de ese hijo
		if (idx!=0 && C.get(idx-1).n>=t)
			borrowFromPrev(idx);

		// Si el siguiente elemento secundario (C[idx+1]) 
		//tiene más de t-1 claves, toma prestada una clave de ese elemento secundario
		else if (idx!=n && C.get(idx+1).n>=t)
			borrowFromNext(idx);

		// Combinar C[idx] con su hermano
		// Si C[idx] es el último hijo, fusionarlo con su hermano anterior
		// De lo contrario, fusionarlo con su próximo hermano
		else{
			if (idx != n)
				merge(idx);// fusiona idx con su sgte idx+1
			else
				merge(idx-1);// fusiona idx- 1 cpn idx
		}
		return;
	}

	// Una función para tomar prestada una clave de C[idx-1] e insertarla en C[idx]
	void borrowFromPrev(int idx){

		BNodeGeneric<T> child=C.get(idx);
		BNodeGeneric<T> sibling=C.get(idx-1);

		// La última clave de C[idx-1] sube al padre y 
		//la clave[idx-1] del padre se inserta como la primera
		//clave en C[idx]. Por lo tanto, el hermano pierde una llave y el niño gana una llave.

		// Mover todas las claves en C[idx] un paso adelante
		for (int i=child.n-1; i>=0; --i)
			child.keys.set(i+1, child.keys.get(i));

		// Si C[idx] no es una hoja, mueve todos sus punteros secundarios un paso adelante
		if (!child.leaf){
			for(int i=child.n; i>=0; --i)
				child.C.set(i+1, child.C.get(i));
		}

		// Establecer la primera clave del niño igual a las claves [idx-1] del nodo actual
		child.keys.set(0, keys.get(idx-1));

		// Mover al último hijo del hermano como primer hijo de C[idx]
		if(!child.leaf)
			child.C.set(0, sibling.C.get(sibling.n));

		// Mover la clave del hermano al padre
		// Esto reduce el número de llaves en el hermano
		keys.set(idx-1, sibling.keys.get(sibling.n-1));

		child.n += 1;
		sibling.n -= 1;

		return;
	}

	// Una función para tomar prestada una clave de C[idx+1] y colocarla en C[idx]
	void borrowFromNext(int idx) {

		BNodeGeneric<T> child=C.get(idx);
		BNodeGeneric<T> sibling=C.get(idx+1);

		// keys[idx] se inserta como la última clave en C[idx]
		child.keys.set(child.n, keys.get(idx));

		// El primer hijo del hermano se inserta como el último hijo en C[idx]
		if (!(child.leaf))
			child.C.set((child.n)+1, sibling.C.get(0));

		//La primera clave del hermano se inserta en claves[idx]
		keys.set(idx, sibling.keys.get(0));

		// Mover todas las claves en hermano un paso atrás
		for (int i=1; i<sibling.n; ++i)
			sibling.keys.set(i-1, sibling.keys.get(i));

		// Mover los punteros secundarios un paso atrás
		if (!sibling.leaf){
			for(int i=1; i<=sibling.n; ++i)
				sibling.C.set(i-1, sibling.C.get(i));
		}

		// Aumentar y disminuir el número de claves de C[idx] y C[idx+1]
		// respectivamente
		child.n += 1;
		sibling.n -= 1;

		return;
	}

	// Una función para fusionar C[idx] con C[idx+1]
	// C[idx+1] se libera después de la fusión
	void merge(int idx) {
		BNodeGeneric<T> child = C.get(idx);//hijo 
		BNodeGeneric<T> sibling = C.get(idx + 1);//hermano

		// Sacando una clave del nodo actual e insertándola en la posición (t-1) de C[idx]
		child.keys.set(t-1, keys.get(idx));// se insereta "k" en removefromnonleaf

		// Copiando las claves de C[idx+1] a C[idx] al final	
		for (int i=0; i<sibling.n; ++i)
			child.keys.set(i+t, sibling.keys.get(i));

		// Copiando los punteros secundarios de C[idx+1] a C[idx]
		if (!child.leaf){
			for(int i=0; i<=sibling.n; ++i)
				child.C.set(i+t, sibling.C.get(i));
		}

		// Mover todas las claves después de idx en el nodo actual un paso antes -
		//para llenar el espacio creado al mover claves[idx] a C[idx]
		for (int i=idx+1; i<n; ++i)
			keys.set(i-1, keys.get(i));

		// Mover los punteros secundarios después de (idx+1) 
		//en el nodo actual un paso antes
		for (int i=idx+2; i<=n; ++i)
			C.set(i-1, C.get(i));

		// Actualizar el conteo de claves del niño y el nodo actual
		child.n += sibling.n+1; //el mas 1 es del keys[idx]
		n--;

		return;
	}
}