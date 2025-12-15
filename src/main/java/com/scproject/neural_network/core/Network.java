package com.scproject.neural_network.core;

import java.util.ArrayList;
import java.util.List;

public class Network {
    private List<Layer> layers;

    public Network() {
        this.layers = new ArrayList<>();
    }

    public void addLayer(Layer layer) {
        layers.add(layer);
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public int getNumLayers() {
        return layers.size();
    }

    public Layer getLayer(int index) {
        return layers.get(index);
    }

    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Neural Network Architecture:\n");
        for (int i = 0; i < layers.size(); i++) {
            Layer layer = layers.get(i);
            sb.append(String.format("Layer %d: %d -> %d [%s]\n",
                    i, layer.getInputSize(), layer.getOutputSize(),
                    layer.getActivation().getClass().getSimpleName()));
        }
        return sb.toString();
    }
}
