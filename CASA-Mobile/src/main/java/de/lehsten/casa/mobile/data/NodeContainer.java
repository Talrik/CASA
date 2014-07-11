package de.lehsten.casa.mobile.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.vaadin.data.util.BeanItemContainer;


public class NodeContainer extends BeanItemContainer<Node> {

    private static final long serialVersionUID = 1L;
    
    public NodeContainer(){
    	super(Node.class);
    }
    
    
    public Collection<? extends Node> getNodes(Object o){
    	List<Node> nodes = new ArrayList<Node>();
    	for (Node node : getAllItemIds()){
    		if (true){
    			nodes.add(node);
    		}
    	}
		return nodes;
    }
    
    public Collection<? extends Node> getNodeTypes(){
    	List<Node> nodeTypes = new ArrayList<Node>();
    		nodeTypes.add(new PrivateNode());
    		nodeTypes.add(new GroupNode());
    		nodeTypes.add(new PublicNode());
    	return nodeTypes;
    }

	
}
