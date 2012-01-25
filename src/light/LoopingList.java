/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package light;

import java.util.ArrayList;

public class LoopingList<E> extends ArrayList<E> {

    public LoopingList() {
        super();
    }

    @Override
    public E get(int index) {
        if(size()==1) index=0;
        else if(index < 0 || index >= size()) index = index % size();
        if(index<0) index = size() + index;
        return super.get(index);
    }
    
    @Override
    public boolean add(E e) {
    	// add a new element and update the last inserted index
    	boolean added = super.add(e);
    	if (added)
    		lastInserted = this.size() - 1;
    	return added;
    };
    
    @Override
    public void add(int index, E element) {
    	super.add(index, element);
    	lastInserted = index;
    };
    
    int lastInserted = 0;
    public void insertBetween(int j, int k, E element)
    {
    	// go from k to j in counter-clockwise direction:
    	int i = k+1;
    	while (i != j)
    	{
    		this.remove(i);
    		i = (i+1) % this.size();
    	}
    	this.add(k+1, element);
    }
    
    public int getLastInserted()
    {
    	return lastInserted;
    }
}
