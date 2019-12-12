#Introducing Indices
Based on https://www.youtube.com/watch?v=7NsXcedg5fo

Notice that the quad reuses two vertices, the top left and bottom right corners.
```
float[] vertices = new float[]{
    -0.5f, 0.5f, 0,
    0.5f, 0.5f, 0,
    0.5f, -0.5f, 0,
    
    0.5f, -0.5f, 0,
    -0.5f, -0.5f, 0,
    -0.5f, 0.5f, 0
};
```
The same goes for the texture coordinates. This might be a problem for more complex models, so we want to specify a
single vertex only once. This can be done using index buffer objects (IBOs), which contain _indices_ of already bound vertices.

The model class needs yet another buffer binding in the constructor, but this time it's an `GL_ELEMENT_ARRAY_BUFFER` 
```
indexId = glGenBuffers();
glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexId);
glBufferData(GL_ELEMENT_ARRAY_BUFFER, createIntBuffer(indices), GL_STATIC_DRAW);
```

For this to work, the `render()` method also needs adjusting.
```
glDrawArrays(GL_TRIANGLES, 0, drawCount);
```
becomes
```
glDrawElements(GL_TRIANGLES, drawCount, GL_UNSIGNED_INT, 0);
``` 
where `drawCount` is set to the number of indices.

Finally, the `vertices` and `textureCoords` array can be pruned of duplicates, and an `indices` array introduced.

```
float[] vertices = new float[]{
        -0.5f, 0.5f, 0,
        0.5f, 0.5f, 0,
        0.5f, -0.5f, 0,
        -0.5f, -0.5f, 0
};

float[] textureCoords = new float[]{
        0, 0,
        1, 0,
        1, 1,
        0, 1,
};

int[] indices = new int[]{
        0, 1, 2,
        2, 3, 0
};
```
We see that there are no duplicated vertices or coordinates and that the vertices are used to describe what to draw.