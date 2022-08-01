public class Test {
	public static void main(String[] args) {
		
		//--/*
		BTree<Integer> t = new BTree<Integer>(2); 
		t.insert(1);
		t.insert(3);
		t.insert(7);
		t.insert(10);
		t.insert(11);
		t.insert(13);
		t.insert(14);
		t.insert(15);
		t.insert(18);
		t.insert(16);
		t.insert(19);
		t.insert(24);
		t.insert(25);
		t.insert(26);
		t.insert(21);
		t.insert(4);
		t.insert(5);
		t.insert(20);
		t.insert(22);
		t.insert(2);
		t.insert(17);
		t.insert(12);
		t.insert(6);
        System.out.print("El recorrido del árbol construido es\n");
        t.traverse();
        System.out.println("\n");
        
        t.remove(6);
        System.out.print("Recorrido del árbol después de eliminar 6\n");
        t.traverse();
        System.out.println("\n");
        
        t.remove(13);
        System.out.print("Recorrido del árbol después de eliminar 13\n");
        t.traverse();
        System.out.println("\n");
        
        t.remove(7);
        System.out.print("Recorrido del árbol después de eliminar 7\n");
        t.traverse();
        System.out.println("\n");
        
        t.remove(4);
        System.out.print("Recorrido del árbol después de eliminar 4\n");
        t.traverse();
        System.out.println("\n");
        
        t.remove(2);
        System.out.print("Recorrido del árbol después de eliminar 2\n");
        t.traverse();
        System.out.println("\n");
                                                                                                                                                   
        t.remove(16);
        System.out.print("Recorrido del árbol después de eliminar 16\n");
        t.traverse();
        System.out.println("\n");
        //--*/
		
		/*
		BTree<String> t = new BTree<String>(2); 
		t.insert("Pepe");
		t.insert("Juan");
		t.insert("Random");
		t.insert("Fernan");
		t.insert("Auron");
		t.insert("Ibai");
		t.insert("Gustavo");
		t.insert("gg");
		System.out.print("El recorrido del árbol construido es\n");
	    t.traverse();
	    System.out.println("\n");
	    System.out.println("Esta dd? " + t.search("dd"));
	    t.remove("Ibai");
        System.out.print("Recorrido del árbol después de eliminar 'Ibai'\n");
        t.traverse();
        System.out.println("\n");
	    */

	}
}