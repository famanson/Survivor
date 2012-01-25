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
    
    public void insertBetween(int j, int k, E element)
    {
    	// go from j to k in clockwise direction:
    	int i = j+1;
    	while (i != k)
    	{
    		this.remove(i);
    		i = (i+1) % this.size();
    	}
    	this.add(j+1, element);
    }
    
}
