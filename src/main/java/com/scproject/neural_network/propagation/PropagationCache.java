package com.scproject.neural_network.propagation;

import com.scproject.neural_network.activations.Activation;

import java.util.HashMap;
import java.util.Map;

public class PropagationCache {

    private double[][] input;
    private Map<Integer, LayerCache> layerCaches;

    public PropagationCache() {
        this.layerCaches = new HashMap<>();
    }

    public double[][] getInput() {
        return input;
    }

    public double[][] getLayerInput(int layerIndex) {
        LayerCache cache = layerCaches.get(layerIndex);
        if (cache == null) {
            throw new IllegalArgumentException("No cache found for layer " + layerIndex);
        }
        return cache.A_prev;
    }

    public void storeInput(double[][] input) {
        this.input = input;
    }

    public void storeLayer(int layerIndex, double[][] A_prev, double[][] Z,
                           double[][] W, double[][] b, Activation activation) {
        LayerCache cache = new LayerCache();
        cache.A_prev = A_prev;
        cache.Z = Z;
        cache.W = W;
        cache.b = b;
        cache.activation = activation;

        layerCaches.put(layerIndex, cache);
    }

    public static class LayerCache {
        public double[][] A_prev;
        public double[][] Z;
        public double[][] W;
        public double[][] b;
        public Activation activation;

        @Override
        public String toString() {
            return String.format(
                    "LayerCache{A_prev: %dx%d, Z: %dx%d, W: %dx%d, b: %dx%d}",
                    A_prev.length, A_prev[0].length,
                    Z.length, Z[0].length,
                    W.length, W[0].length,
                    b.length, b[0].length
            );
        }
    }

    public double[][] getLayerZ(int layerIndex) {
        LayerCache cache = layerCaches.get(layerIndex);
        if (cache == null) {
            throw new IllegalArgumentException("No cache found for layer " + layerIndex);
        }
        return cache.Z;
    }

    public double[][] getLayerWeights(int layerIndex) {
        LayerCache cache = layerCaches.get(layerIndex);
        if (cache == null) {
            throw new IllegalArgumentException("No cache found for layer " + layerIndex);
        }
        return cache.W;
    }

    public double[][] getLayerBias(int layerIndex) {
        LayerCache cache = layerCaches.get(layerIndex);
        if (cache == null) {
            throw new IllegalArgumentException("No cache found for layer " + layerIndex);
        }
        return cache.b;
    }

    public Activation getLayerActivation(int layerIndex) {
        LayerCache cache = layerCaches.get(layerIndex);
        if (cache == null) {
            throw new IllegalArgumentException("No cache found for layer " + layerIndex);
        }
        return cache.activation;
    }

    public void clear() {
        input = null;
        layerCaches.clear();
    }

    public int getNumOfCashedLayers() {
        return layerCaches.size();
    }

    public boolean hasCashLayer(int layerIndex) {
        return layerCaches.containsKey(layerIndex);
    }

    public LayerCache getLayerCache(int layerIndex) {
        return layerCaches.get(layerIndex);
    }
}