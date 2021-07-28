
/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 *
 */

public class AVLTree { 
	private final IAVLNode VIRTUAL = new AVLNode(-1, null);
	private IAVLNode root;
	private IAVLNode min; //node with minimal key
	private IAVLNode max; // node with max key
	
public AVLTree() { //O(1)
	this.root = VIRTUAL;
	this.min = null;
	this.max = null;
}
//build's a subtree from node
public AVLTree(IAVLNode node) { //O(log(n))
	this.root = node;
	this.root.setParent(null);
	this.min = getMin(node);
	this.max = getMax(node);
}

	
  /**
   * public boolean empty()
   *
   * returns true if and only if the tree is empty
   *
   */
  public boolean empty() {   //O(1)
	  if(this.root.getKey() == VIRTUAL.getKey()) {
		  return true;
	  }
    return false;
  }

 /**
   * public String search(int k)
   *
   * returns the info of an item with key k if it exists in the tree
   * otherwise, returns null
   */
  public String search(int k)  //O(log(n))
  {
	  IAVLNode currRoot = this.root;
	  if(currRoot.getKey() == VIRTUAL.getKey()) {
		  return null;
	  }
	  while(currRoot.getKey() != -1) {
		  if (currRoot.getKey() == k ) {
			  return currRoot.getValue();
		  }
		  if (k > currRoot.getKey()) {
			  currRoot = currRoot.getRight();
		  }
		  else if (k < currRoot.getKey()) {
			  currRoot = currRoot.getLeft();
		  }
	  }
	  return null;
  }

  /**
   * public int insert(int k, String i)
   *
   * inserts an item with key k and info i to the AVL tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
   * promotion/rotation - counted as one rebalnce operation, double-rotation is counted as 2.
   * returns -1 if an item with key k already exists in the tree.
   */
   public int insert(int k, String i) { //O(log(n))
	    int cnt = 0;
	   	IAVLNode toInsert = new AVLNode(k,i);
	   	if(search(k) != null) { 
	   		return -1;
	   	}
	   	if(this.getRoot().getSize() == 0) {
	   		this.root = toInsert;
	   		this.min = this.root;
	   		this.max = this.root;
	   		return 0;
	   	}
	   	else {
		   	IAVLNode Root = this.root;
	   		IAVLNode insertUnder = treePosition(Root, k);
		   	updateMinMaxInsert(toInsert);
	   		if(toInsert.getKey() < insertUnder.getKey() ) {
	   			insertUnder.setLeft(toInsert);
	   			toInsert.setParent(insertUnder);
	   		}
	   		else {
	   			insertUnder.setRight(toInsert);
	   			toInsert.setParent(insertUnder);
	   		}
		   	updateSize(insertUnder, 1);
		   	cnt = rebalanceInsert(insertUnder);
	   	}
	  return cnt;	
   }

  /**
   * public int delete(int k)
   *
   * deletes an item with key k from the binary tree, if it is there;
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
   * demotion/rotation - counted as one rebalnce operation, double-rotation is counted as 2.
   * returns -1 if an item with key k was not found in the tree.
   */
   
   public int delete(int k) //O(log(n))
   {
	   int cnt = 0;
	   IAVLNode toDelete = findNode(k);
	   if(this.root.getKey() == -1) { // empty tree
		   return -1;
	   }
	   if(findNode(k).getKey() == -1) { // node not in tree
		   return -1;
	   }
	   // node k is in tree
	   if(this.root.getKey() == toDelete.getKey() && this.root.getSize() == 1) { //tree with one node, k
		   this.root = VIRTUAL;   
		   this.min = null;
		   this.max = null;
		   return cnt;
	   }
	   updateMinMaxDelete(toDelete);
	   IAVLNode curr = fixPointerDelete(toDelete); //delete the item
	   if(curr != null) {
		   updateSize(curr,-1);
	   }
	   else { //if curr == null then we deleted the root
		   this.root.setSize(this.root.getLeft().getSize() + this.root.getRight().getSize() + 1);
	   }
	   cnt = rebalanceDelete(curr);
	   return cnt;	
   }

   /**
    * public String min()
    *
    * Returns the info of the item with the smallest key in the tree,
    * or null if the tree is empty
    */
   public String min() //O(1)
   {
	   String val = null;
	   if(this.min == null ) {
		   return null;
	   }
	   else {
		   val = this.min.getValue(); 
	   }
	   return val;
   }

   /**
    * public String max()
    *
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty
    */
   public String max() //O(1)
   {
	   String val = null;
	   if(this.max == null) {
		   return null;
	   }
	   else {
		   val = this.max.getValue(); 
	   }
	   return val;  
	}

  /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   */
  public int[] keysToArray()  //O(log(n))
  {
	  int[] arr = new int[this.size()];
	  int index = 0;
	  IAVLNode currNode = this.root;
	  
	  inOrderKeys(currNode, arr, index);
	  
	  return arr;                
  }

  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   */
  public String[] infoToArray() //O(log(n))
  {
        String[] arr = new String[this.size()]; 
        int index = 0;
        IAVLNode currNode = this.root;
        
        inOrderInfo(currNode, arr, index);

        return arr;                    
  }

   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
    *
    * precondition: none
    * postcondition: none
    */
   public int size() //O(1)
   {
	   return this.root.getSize(); 
   }
   
     /**
    * public int getRoot()
    *
    * Returns the root AVL node, or null if the tree is empty
    *
    * precondition: none
    * postcondition: none
    */
   public IAVLNode getRoot() //O(1)
   {
	   return this.root;
   }
   
     /**
    * public string split(int x)
    *
    * splits the tree into 2 trees according to the key x. 
    * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
	  * precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
    * postcondition: none
    */   
   public AVLTree[] split(int x)  //O(log(n))
   {
	   IAVLNode curr = findNode(x);
	   AVLTree small = new AVLTree(curr.getLeft());
	   AVLTree big = new AVLTree(curr.getRight());
	   IAVLNode parent = curr.getParent();
	   AVLTree add = new AVLTree(); 
	   while(parent != null) {
		   if(curr.getKey() == parent.getRight().getKey()) { //curr is a right child
			   curr = parent;
			   parent = parent.getParent();
			   if(curr.getLeft().getKey() != -1) {
				   add = new AVLTree(curr.getLeft());
			   }
			   curr.setParent(null);
			   curr.setRight(VIRTUAL);
			   curr.setLeft(VIRTUAL);
			   curr.setSize(1);
			   curr.setHeight(0);
			   small.join(curr, add);
		   }
		   else { // curr is a left child
			   curr = parent;
			   parent = parent.getParent();
			   if(curr.getRight().getKey() != -1) {
				   add = new AVLTree(curr.getRight());
			   }
			   curr.setParent(null);
			   curr.setRight(VIRTUAL);
			   curr.setLeft(VIRTUAL);
			   curr.setSize(1);
			   curr.setHeight(0);
			   big.join(curr, add);   
		   }
	   }
	   AVLTree[] arr = new AVLTree[2];
	   arr[0] = small;
	   arr[1] = big;
	   return arr; 
   }
   
   /**
    * public join(IAVLNode x, AVLTree t)
    *
    * joins t and x with the tree. 	
    * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
	  * precondition: keys(x,t) < keys() or keys(x,t) > keys(). t/tree might be empty (rank = -1).
    * postcondition: none
    */   
   public int join(IAVLNode x, AVLTree t) //O(log(|Rank.this - Rank.t| + 1)
   {
	   AVLTree small = new AVLTree();
	   AVLTree big = new AVLTree();
	   
	   if(this.root.getKey() < x.getKey()) {
		   small = this;
		   big = t; 
	   }
	   else {
		   small = t;
		   big = this;
	   }
	   if(this.empty()) {
		   this.root = t.root;
		   this.min = t.min;
		   this.max = t.max;
		   this.insert(x.getKey(), x.getValue());
		   return this.root.getHeight() + 1;
		   
	   }
	   if(t.empty()){
		  this.insert(x.getKey(), x.getValue());
		  return this.root.getHeight() + 1;
	
	   }
	   if(this.empty() && t.empty()) {
		   this.root = x;
		   this.min = x;
		   this.max = x;
		   x.setParent(null);
		   x.setLeft(VIRTUAL);
		   x.setRight(VIRTUAL);
		   x.setHeight(0);
		   return this.root.getHeight() + 1;
	   }
	   
	   if(big.root.getHeight() == small.root.getHeight()) {
		   x.setSize(small.root.getSize() + big.getRoot().getSize() + 1);
		   x.setHeight(big.root.getHeight() + 1);
		   x.setLeft(small.root);
		   x.setRight(big.root);
		   x.setParent(null);
		   this.root = x;
		   this.min = small.min;
		   this.max = big.max;
		   return 1;
	   }
	   
	   int addSize = 0;
	   int diff = 0;
	   if(big.root.getHeight() > small.root.getHeight()) { //big is higher
		  diff = big.root.getHeight() - small.root.getHeight();
		   addSize = small.size() + 1;
		  IAVLNode curr = big.root;
		  IAVLNode parent = curr.getParent();
		   while(curr.getHeight() > small.root.getHeight()) { //will stop with curr with height like small.root or smaller by 1
			   parent = curr;
			   curr = curr.getLeft();
		   }
		   x.setLeft(small.root);
		   x.setRight(curr);
		   parent.setLeft(x);
		   small.root.setParent(x);
		   x.setParent(parent);
		   curr.setParent(x);
		   updateHeight(x);
		   x.setSize(x.getLeft().getSize() + x.getRight().getSize() + 1);
		   this.root = big.root;
		   this.min = small.min;
		   this.max = big.max;
		   
	   }
	   else if(small.root.getHeight() > big.root.getHeight()) { // small is higher
		  diff = small.root.getHeight() - big.root.getHeight();
		  addSize = big.size() + 1;
		  IAVLNode curr = small.root;
		  IAVLNode parent = curr.getParent();
		  while(curr.getHeight() > big.root.getHeight()) { //will stop with curr with height like big.root or smaller by 1
			  parent = curr; 
			  curr = curr.getRight();
		   }
		   x.setSize(big.root.getSize() + curr.getSize() + 1);
		   x.setRight(big.root);
		   x.setLeft(curr);
		   parent.setRight(x);
		   big.root.setParent(x);
		   x.setParent(parent);
		   curr.setParent(x);
		   updateHeight(x);
		   x.setSize(x.getLeft().getSize() + x.getRight().getSize() + 1);
		   this.root = small.root;
		   this.min = small.min;
		   this.max = big.max;
	   }
	   updateSize(x.getParent(), addSize);
	   rebalanceInsert(x.getParent());
	   
	   return diff + 1; 
   }
   
   
   /////////////////FUNCTIONS WE ADDED///////////////////////////

   /**
    * @pre key k in not in tree
    * @post returned node is a leaf if key k is not in the tree
    */ 
   public IAVLNode treePosition(IAVLNode x, int k) {  //O(log(n)) 
	   IAVLNode currNode = x;
	   while( x.getKey() != VIRTUAL.getKey()) {
		   if(k < x.getKey() ) {
			   currNode = x;
			   x = x.getLeft();   
		   }
		   else {
			   currNode = x;
			   x = x.getRight();
		   }
	   }
	   return currNode;
   }
   
   /**
    * finds the node with int k
    * if node in tree - return it
    * else return VIRTUAL
    */
   public IAVLNode findNode(int k) { //O(log(n))
	   if(this.root.getKey() == -1 ) { //empty tree
		   return VIRTUAL;
	   }
	   else {
		   IAVLNode curr = this.root;
		   while(curr.getKey() != -1) {
			   if(curr.getKey() == k) {
				   return curr;
			   }
			   else if(curr.getKey() > k){
				   curr = curr.getLeft();
			   }
			   else {
				   curr = curr.getRight();
			   }
		   }
		   return VIRTUAL; //if got here than node not found
	   }
   }
   /**
    * 
    * @param node
    * @return return min node in node's sub tree
    */
   public IAVLNode getMin(IAVLNode node) { //O(log(n))
	   if(node.getKey() != -1) {
		   while(node.getLeft().getKey() != -1) {
			   node = node.getLeft();
		   }
	   }
	   return node;
   }
   /**
    * 
    * @param node
    * @return max node in node's subtree
    */
   public IAVLNode getMax(IAVLNode node) {  //O(log(n))
	   if(node.getKey() != -1) {
		   while(node.getRight().getKey() != -1) {
			   node = node.getRight();
		   }
	   }
	   return node;
   }
   
   public void updateMinMaxInsert(IAVLNode toInsert) { //O(1)
	   if(toInsert.getKey() < this.min.getKey()) {
	   		this.min = toInsert;
	   	}
	   else if(toInsert.getKey() > this.max.getKey()) {
	   		this.max = toInsert;
	   	}
   }
   
   public void rotateRight(IAVLNode upper, IAVLNode lower) { //O(1)
	   if(this.root.getKey() == upper.getKey()) {
		   lower.setParent(null);
		   upper.setParent(lower);
		   upper.setLeft(lower.getRight());
		   lower.getRight().setParent(upper);
		   lower.setRight(upper);
		   this.root = lower;
		   
	   }
	   else {
		   	if(upper.getParent().getLeft().getKey() == upper.getKey()) { //upper is left child
			   lower.setParent(upper.getParent());
			   upper.getParent().setLeft(lower);
			   upper.setParent(lower);
			   upper.setLeft(lower.getRight());
			   lower.getRight().setParent(upper);
			   lower.setRight(upper);
		   }
		   	else { //upper is right child
				   lower.setParent(upper.getParent());
				   upper.getParent().setRight(lower);
				   upper.setParent(lower);
				   upper.setLeft(lower.getRight());
				   lower.getRight().setParent(upper);
				   lower.setRight(upper);
		   		
		   	}
		   
	   }
	   upper.setSize(upper.getLeft().getSize() + upper.getRight().getSize() + 1);
	   lower.setSize(lower.getLeft().getSize() + lower.getRight().getSize() + 1);
   }
   
   public void rotateLeft(IAVLNode upper, IAVLNode lower) { //O(1)
	   if(this.root.getKey() == upper.getKey()) {
		   lower.setParent(null);
		   upper.setParent(lower);
		   upper.setRight(lower.getLeft());
		   lower.getLeft().setParent(upper);
		   lower.setLeft(upper);
		   this.root = lower;	   
	   }

	   else {
		   if(upper.getParent().getLeft().getKey() == upper.getKey()) {  //upper is left child
			   lower.setParent(upper.getParent());
			   upper.getParent().setLeft(lower);
			   upper.setParent(lower);
			   upper.setRight(lower.getLeft());
			   lower.getLeft().setParent(upper);
			   lower.setLeft(upper);
		   }
		   else { //upper is right child
			   lower.setParent(upper.getParent());
			   upper.getParent().setRight(lower);
			   upper.setParent(lower);
			   upper.setRight(lower.getLeft());
			   lower.getLeft().setParent(upper);
			   lower.setLeft(upper);
		   }

	   }
	   upper.setSize(upper.getLeft().getSize() + upper.getRight().getSize() + 1);
	   lower.setSize(lower.getLeft().getSize() + lower.getRight().getSize() + 1);
   }
   
   public void updateSize(IAVLNode node, int add) {  //O(log(n))  
	   while(node != null) {
		   node.setSize(node.getSize() + add);
		   node = node.getParent();
	   }
   }
   
   public void updateHeight(IAVLNode curr) {  //O(1)
	   int height = Math.max(curr.getLeft().getHeight(), curr.getRight().getHeight()) + 1;
	   curr.setHeight(height);
	   
   }
   
   public int rebalanceInsert(IAVLNode insertUnder) { //O(log(n))
	   int cnt = 0; 
	       
	   		// insertUnder was a leaf and now has one VIRTUAL child
	   
		   while(insertUnder != null && !(insertUnder.getHeight() - insertUnder.getLeft().getHeight() != 0 && insertUnder.getHeight() - insertUnder.getRight().getHeight() != 0 )) {
				   	
			   ///LEFT CASES///
			   
			   if(insertUnder.getHeight() - insertUnder.getLeft().getHeight() == 0 && insertUnder.getHeight() - insertUnder.getRight().getHeight() == 1) {
				   		updateHeight(insertUnder);
				   		insertUnder = insertUnder.getParent();
				   		cnt++; 
			   }
			   
			   if((insertUnder != null) &&  ( insertUnder.getHeight() - insertUnder.getLeft().getHeight() == 0 && insertUnder.getHeight() - insertUnder.getRight().getHeight() == 2) ) {
			   		IAVLNode leftChild = insertUnder.getLeft();
			   		if(leftChild.getHeight() - leftChild.getLeft().getHeight() == 1 && leftChild.getHeight() - leftChild.getRight().getHeight() == 2) {
			   			rotateRight(insertUnder,leftChild);
			   			updateHeight(insertUnder);
			   			cnt = cnt + 2;
			   		}
			   		else if(leftChild.getHeight() - leftChild.getLeft().getHeight() == 2 && leftChild.getHeight() - leftChild.getRight().getHeight() == 1) {
			   			rotateLeft(leftChild, leftChild.getRight());
			   			rotateRight(insertUnder, insertUnder.getLeft()); // use this arguments because now inserUnder's left child changed after last rotation.
			   			updateHeight(insertUnder);      //update height == promote/demote from bottom to top
			   			updateHeight(insertUnder.getParent().getLeft());
			   			updateHeight(insertUnder.getParent());
			   			cnt = cnt + 5;
			   		}
			   		else if(leftChild.getHeight() - leftChild.getLeft().getHeight() == 1 && leftChild.getHeight() - leftChild.getRight().getHeight() == 1) { //will happen only after join
			   			rotateRight(insertUnder,leftChild);
			   			cnt = cnt + 1;
			   		}
			   	}
				   	
			///RIGHT CASES///
			   
			   	if((insertUnder != null) && ( insertUnder.getHeight() - insertUnder.getLeft().getHeight() == 1 && insertUnder.getHeight() - insertUnder.getRight().getHeight() == 0) ) {
			   		updateHeight(insertUnder);
			   		insertUnder = insertUnder.getParent();
			   		cnt++; 
			   	}
			   	if((insertUnder != null) && ( insertUnder.getHeight() - insertUnder.getLeft().getHeight() == 2 && insertUnder.getHeight() - insertUnder.getRight().getHeight() == 0) ) { //first if might make insertUnder null so make sure for this if that it is not null
			   		IAVLNode rightChild = insertUnder.getRight();
			   		if(rightChild.getHeight() - rightChild.getLeft().getHeight() == 2 && rightChild.getHeight() - rightChild.getRight().getHeight() == 1) {
			   			rotateLeft(insertUnder, rightChild);
			   			updateHeight(insertUnder);
			   			cnt = cnt + 2;
			   		}
			   		else if(rightChild.getHeight() - rightChild.getLeft().getHeight() == 1 && rightChild.getHeight() - rightChild.getRight().getHeight() == 2) {
			   			rotateRight(rightChild, rightChild.getLeft());
			   			rotateLeft(insertUnder, insertUnder.getRight()); // use this arguments because now inserUnder's right child changed after last rotation.
			   			updateHeight(insertUnder);      //update height == promote/demote from bottom to top
			   			updateHeight(insertUnder.getParent().getRight());
			   			updateHeight(insertUnder.getParent());
			   			cnt = cnt + 5;
			   		}
			   		else if(rightChild.getHeight() - rightChild.getLeft().getHeight() == 1 && rightChild.getHeight() - rightChild.getRight().getHeight() == 1) { //will happen only after join
			   			rotateLeft(insertUnder, rightChild);
			   			cnt = cnt + 1;
			   		}
			   	}
		   } //while closer
	   
	   return cnt;
   }
   /**
    * @pre node is the root of the tree
    * @pre arr is an empty Array
    * @pre index is 0
    */
   
   public int inOrderKeys(IAVLNode node, int[] arr, int index) //O(log(n))
   {  
	   if(node.getKey() == -1) {
		   return index;
	   }
	   
	   index = inOrderKeys(node.getLeft(), arr , index);
	   arr[index] = node.getKey();
	   index++;
	   index = inOrderKeys(node.getRight(), arr, index);
	   
	   return index;
	   
   }
   
   public int inOrderInfo(IAVLNode node, String[] arr, int index) //O(log(n))
   {
	   if(node.getKey() == -1) {
		   return index;
	   }
	   
	   index = inOrderInfo(node.getLeft(), arr , index);
	   arr[index] = node.getValue();
	   index++;
	   index = inOrderInfo(node.getRight(), arr, index);
	   
	   return index;
   }
   
   public IAVLNode successor(IAVLNode node) {  //O(log(n))
	   if(node.getRight().getKey() != -1) { //has a right child
		   node = node.getRight();
		   while(node.getLeft().getKey() != -1) {
			   node = node.getLeft();
		   }
		   return node; 
	   }
	   else {
		   IAVLNode parent = node.getParent();
		   while(parent != null && node.getKey() == parent.getRight().getKey()) {
			   node = parent;
			   parent = node.getParent();
		   }
		   return parent;
		   
	   }
   }
   
   public IAVLNode predecessor(IAVLNode node) {  //O(log(n))
	   if(node.getLeft().getKey() != -1 ) { //has left child
		   node = node.getLeft();
		   while(node.getRight().getKey() != -1) {
			   node = node.getRight();
		   }
		   return node;
	   }
	   else { 
		   IAVLNode parent = node.getParent();
		   while(parent != null && node.getKey() == parent.getLeft().getKey()) {
			   node = parent;  
			   parent = node.getParent();
		   }
		   return parent;
	   }
   }
   
   public void updateMinMaxDelete(IAVLNode toDelete) { //O(log(n))
	  if(this.root.getSize() == 1) { //one node left in tree
		   this.min = this.root;
		   this.max = this.root;
		   
	   }
	   if(toDelete.getKey() == this.min.getKey()) { // toDelete is the min node
		   this.min = successor(toDelete);
		   
	   }
	   if(toDelete.getKey() == this.max.getKey()) { //toDelete is the max node
		   this.max = predecessor(toDelete);
	   }  
   }
   
   /**
    * 
    * @param toDelete
    * @return the parent of to delete (null if size was 1 before delete)
    */
   public IAVLNode fixPointerDelete(IAVLNode toDelete) { //O(log(n))
	   
	   IAVLNode parent = toDelete.getParent();
	   if(toDelete.getLeft().getKey() == -1 && toDelete.getRight().getKey() == -1) {   //toDelete is a leaf
		   if(toDelete.getKey() == toDelete.getParent().getRight().getKey()) { //toDelete is right child
			   parent.setRight(VIRTUAL);
		   }
		   else { //toDelete is left child
			   parent.setLeft(VIRTUAL);
		   }
		   return parent;
	   } //if closer
	   
	   if(toDelete.getLeft().getKey() != -1 && toDelete.getRight().getKey() == -1 ){ //toDelete in unary node with left child
		   if(parent == null) {
			   this.root = toDelete.getLeft();
			   this.root.setParent(null);
			   return null;
		   }
		   else {
			   if(toDelete.getKey() == parent.getLeft().getKey()) { //toDetele is a left child of parent
				   parent.setLeft(toDelete.getLeft());
				   toDelete.getLeft().setParent(parent);
			   }
			   else { //toDelete is right child of parent
				   parent.setRight(toDelete.getLeft());
				   toDelete.getLeft().setParent(parent);
			   }
			   return parent;
		   }
	   } // big if closer
	   
	   if(toDelete.getLeft().getKey() == -1 && toDelete.getRight().getKey() != -1 ){ //toDelte in unary node with right child
		   if(parent == null) {
			   this.root = toDelete.getRight();
			   this.root.setParent(null);
			   return null;
		   }
		   else {
			   if(toDelete.getKey() == parent.getLeft().getKey()) { //toDetele is a left child of parent
				   parent.setLeft(toDelete.getRight());
				   toDelete.getRight().setParent(parent);
			   }
			   else { //toDelete is right child of parent
				   parent.setRight(toDelete.getRight());
				   toDelete.getRight().setParent(parent);
			   }
		   }
		   return parent;
	   } //big if closer
	   
	   IAVLNode successor = successor(toDelete);
	   IAVLNode sucParent = successor.getParent();
	   if(successor.getKey() != toDelete.getRight().getKey()) { //successor is NOT toDelete right child
			toDelete.getRight().setParent(successor);
			sucParent.setLeft(successor.getRight());
			successor.getRight().setParent(sucParent);
			successor.setRight(toDelete.getRight());
			successor.setLeft(toDelete.getLeft());
			toDelete.getLeft().setParent(successor);
			
		   if(parent == null) {
			   successor.setParent(null);
			   this.root = successor;
		   }
		   else if(toDelete.getKey() == parent.getLeft().getKey()) { //toDelete if left child
			   parent.setLeft(successor);
			   successor.setParent(parent);
			   
		   }
		   else {
			   parent.setRight(successor);
			   successor.setParent(parent);
			   
		   }
		   successor.setHeight(toDelete.getHeight());
		   successor.setSize(toDelete.getSize());
		   return sucParent;
	   } // if closer
	   
	   else { //successor is toDelete right child
		   successor.setLeft(toDelete.getLeft());
		   toDelete.getLeft().setParent(successor);
		   if(parent == null) {
			   successor.setParent(null);
			   this.root = successor;
		   }
		   else if(toDelete.getKey() == parent.getLeft().getKey()) { //toDelete if left child
			   parent.setLeft(successor);
			   successor.setParent(parent);
			   
		   }
		   else {
			   parent.setRight(successor);
			   successor.setParent(parent);
			   
		   }
		   successor.setHeight(toDelete.getHeight());
		   successor.setSize(toDelete.getSize() - 1);
		   return successor;
	   } //else-if closer 
   }
   
   public int rebalanceDelete(IAVLNode curr) { //O(log(n))
	   int cnt = 0;
	   while (curr != null ) {
		   if((curr.getHeight() - curr.getLeft().getHeight() == 2 && curr.getHeight() - curr.getRight().getHeight() == 1 ) || (curr.getHeight() - curr.getLeft().getHeight() == 1 && curr.getHeight() - curr.getRight().getHeight() == 2 )) {
			   return 0; //deleted a leaf and all is good
		   }
		   if(curr.getHeight() - curr.getLeft().getHeight() == 2 && curr.getHeight() - curr.getRight().getHeight() == 2 ) {
			   updateHeight(curr);
			   cnt++;
			   curr = curr.getParent();
		   }
		  /////// LEFT CASES ////////
		   if((curr != null) &&(curr.getHeight() - curr.getLeft().getHeight() == 3 && curr.getHeight() - curr.getRight().getHeight() == 1)) {
			   IAVLNode rightChild = curr.getRight();
			   if(rightChild.getHeight() - rightChild.getLeft().getHeight() == 1 && rightChild.getHeight() - rightChild.getRight().getHeight() == 1 ) {
				   rotateLeft(curr,rightChild);
				   updateHeight(curr);
				   updateHeight(rightChild);
				   cnt = cnt + 3;
			   }
			   else if(rightChild.getHeight() - rightChild.getLeft().getHeight() == 2 && rightChild.getHeight() - rightChild.getRight().getHeight() == 1) {
				   rotateLeft(curr,rightChild);
				   updateHeight(curr); // does 2 demotes
				   cnt = cnt + 3;  
				   curr = curr.getParent().getParent();
			   }
			   else if(rightChild.getHeight() - rightChild.getLeft().getHeight() == 1 && rightChild.getHeight() - rightChild.getRight().getHeight() == 2) {
				   rotateRight(rightChild,rightChild.getLeft());
				   rotateLeft(curr, curr.getRight());
				   updateHeight(curr); // does 2 demotes
				   updateHeight(rightChild); 
				   updateHeight(curr.getParent());
				   cnt = cnt + 6;
				   curr = curr.getParent().getParent();
			   }
		   }
		/////// RIGHT CASES ////////
		   if((curr != null) &&(curr.getHeight() - curr.getLeft().getHeight() == 1 && curr.getHeight() - curr.getRight().getHeight() == 3)) {
			   IAVLNode leftChild = curr.getLeft();
			   if(leftChild.getHeight() - leftChild.getLeft().getHeight() == 1 && leftChild.getHeight() - leftChild.getRight().getHeight() == 1 ) {
				   rotateRight(curr,leftChild);
				   updateHeight(curr);
				   updateHeight(leftChild);
				   cnt = cnt + 3;
			   }
			   else if(leftChild.getHeight() - leftChild.getLeft().getHeight() == 1 && leftChild.getHeight() - leftChild.getRight().getHeight() == 2) {
				   rotateRight(curr,leftChild);
				   updateHeight(curr); // does 2 demotes
				   cnt = cnt + 3;  
				   curr = curr.getParent().getParent();
			   }
			   else if(leftChild.getHeight() - leftChild.getLeft().getHeight() == 2 && leftChild.getHeight() - leftChild.getRight().getHeight() == 1) {
				   rotateLeft(leftChild,leftChild.getRight());
				   rotateRight(curr, curr.getLeft());
				   updateHeight(curr); // does 2 demotes
				   updateHeight(leftChild); 
				   updateHeight(curr.getParent());
				   cnt = cnt + 6;
				   curr = curr.getParent().getParent();
			   }
		   }
		   curr = curr;
	   }
	   
	   return cnt;
   }
   

	/**
	   * public interface IAVLNode
	   * ! Do not delete or modify this - otherwise all tests will fail !
	   */
	public interface IAVLNode{	
		public int getKey(); //returns node's key (for virtuval node return -1)
		public String getValue(); //returns node's value [info] (for virtuval node return null)
		public void setLeft(IAVLNode node); //sets left child
		public IAVLNode getLeft(); //returns left child (if there is no left child return null)
		public void setRight(IAVLNode node); //sets right child
		public IAVLNode getRight(); //returns right child (if there is no right child return null)
		public void setParent(IAVLNode node); //sets parent
		public IAVLNode getParent(); //returns the parent (if there is no parent return null)
		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node
    	public void setHeight(int height); // sets the height of the node
    	public int getHeight(); // Returns the height of the node (-1 for virtual nodes)
    	public int getSize();
    	public void setSize(int i);
    	
	}

   /**
   * public class AVLNode
   *
   * If you wish to implement classes other than AVLTree
   * (for example AVLNode), do it in this file, not in 
   * another file.
   * This class can and must be modified.
   * (It must implement IAVLNode)
   */
  public class AVLNode implements IAVLNode{
	  private int size;
	  private int height;
	  private int key;
	  private String info;
	  private IAVLNode parent;
	  private IAVLNode left;
	  private IAVLNode right;
 
	  	public AVLNode(int key,String info) {
	  		this.key = key;
	  		this.info = info;
	  		this.parent = null;
	  		if(this.key != -1) {
	  			this.left = VIRTUAL;
	  			this.right = VIRTUAL;
	  			this.height = 0;
	  			this.size = 1;
	  		}
	  		else {
	  			this.left = null;
	  			this.right = null;
	  			this.height = -1;
	  			this.size = 0;
	  		}
	  	}

	  	public int getSize() {
	  		return this.size;
	  	}
	  	
	  	public void setSize(int size) {
	  		this.size = size;	
	  	}
	  
		public int getKey() 
		{
			return this.key; 
		}
		public String getValue()
		{
			return this.info; 
		}
		public void setLeft(IAVLNode node)
		{
			this.left = node;
		}
		public IAVLNode getLeft()
		{
			return this.left; 
		}
		public void setRight(IAVLNode node)
		{
			this.right = node;
		}
		public IAVLNode getRight()
		{
			return this.right;
		}
		public void setParent(IAVLNode node)
		{
			this.parent = node;
		}
		public IAVLNode getParent()
		{
			return this.parent;
		}
		public boolean isRealNode()
		{
			if(this.key == -1) {
				return false;
			}
			return true;
		}
	    public void setHeight(int height)
	    {
	    	this.height = height;
	    }
	    public int getHeight()
	    {
	      return this.height; 
	    }
	  }

}
  
