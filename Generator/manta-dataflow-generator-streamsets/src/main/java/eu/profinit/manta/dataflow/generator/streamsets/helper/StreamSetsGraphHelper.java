package eu.profinit.manta.dataflow.generator.streamsets.helper;

import eu.profinit.manta.connector.streamsets.model.IStreamSetsServer;
import eu.profinit.manta.dataflow.generator.common.helper.AbstractGraphHelper;
import eu.profinit.manta.dataflow.model.Graph;
import eu.profinit.manta.dataflow.model.Node;
import eu.profinit.manta.dataflow.model.NodeType;
import eu.profinit.manta.dataflow.model.Resource;
import eu.profinit.manta.dataflow.model.impl.ResourceImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper provides methods that allows easier construction of data flow graph.
 *
 * @author mburdel
 */
public class StreamSetsGraphHelper extends AbstractGraphHelper {

    /** Cache for nodes corresponding with StreamSets's objects. */
    private final Map<Object, Node> nodeCache = new HashMap<>();

    private IStreamSetsServer server;

    public StreamSetsGraphHelper(Graph graph, Resource scriptResource, IStreamSetsServer server) {
        super(graph, scriptResource, scriptResource, NodeType.COLUMN_FLOW);
        this.server = server;
    }

    /**
     * Method adds node to graph and puts StreamSets object with created node into node's cache.
     *
     * @param object StreamSets object
     * @param name node name
     * @param type node type
     * @param parent node parent
     * @return added node
     */
    public Node buildNode(Object object, String name, NodeType type, Node parent) {
        Node result = addNode(name, type, parent, getScriptResource());
        nodeCache.put(object, result);
        return result;
    }

    /**
     * Method creates nodes for application and service according to parameters and adds its to graph.
     * @param resourceName resource name
     * @param resourceType resource type
     * @param resourceDescription resource description
     * @param applicationName application name
     * @param serviceName service name
     * @return created service's node
     */
    public Node createServiceNode(String resourceName, String resourceType, String resourceDescription,
            String applicationName, String serviceName) {

        Resource resource = new ResourceImpl(resourceName, resourceType, resourceDescription);
        Node application = getGraph().addNode(applicationName, NodeType.APPLICATION.getId(), null, resource);
        Node service = getGraph().addNode(serviceName, NodeType.SERVICE.getId(), application, resource);

        return service;
    }

    /**
     *
     * @param object StreamSets object
     * @return mapped node to StreamSets object from node's cache
     */
    public Node getNode(Object object) {
        return nodeCache.get(object);
    }

    public Graph graph() {
        return getGraph();
    }

    public IStreamSetsServer getServer() {
        return server;
    }

}
