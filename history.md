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

#Adding a Shader
The `Shader` class contains code that loads a vertex and fragment shader, compiles, links, and verifies the resulting 
program. At this point, the shaders are as simple as they come, and the purpose of the exercise is to make things work.

#Readding the Texture
Here, the big thing is changing the way the vertex shader operates. Now, 
it receives two paramaters `vertices` and `textures` and a couple of calls
are swapped out.

```
glEnableClientState(GL_VERTEX_ARRAY);
glEnableClientState(GL_TEXTURE_COORD_ARRAY);
```
become

```
glEnableVertexAttribArray(0);
glEnableVertexAttribArray(1);
```

and
``` 
glVertexPointer(3, GL_FLOAT, 0, 0);
glTexCoordPointer(2, GL_FLOAT, 0, 0); // Will be used by glDrawArrays
```
become more generic:
``` 
glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
```
According to https://www.khronos.org/opengl/wiki/Type_Qualifier_%28GLSL%29 `varying` is equivalent to the input of a fragment shader or the output of a vertex shader. It cannot be used in any other shader stages. 
Vertex shader:
```
#version 120

attribute vec3 vertices;
attribute vec2 textures;

varying vec2 tex_coords;

void main() {
    tex_coords = textures;
    gl_Position = vec4(vertices, 1);
}
```
It gets both the vertices and texture coordinates via attributes. The texture coordinates are just passed on to the 
next shader. In this context, `varying` means out.  

Fragment shader:
```
#version 120

uniform sampler2D sampler;

varying vec2 tex_coords;

void main() {
    gl_FragColor = texture2D(sampler, tex_coords);
}
```
`uniform` means roughly "global" or "user supplied". A sampler is in fact a texture.
`varying` here means "in". The texture coordinates have been set previously by the vertex shader.

Then it's just a matter of picking a pixel from the texture.

## Additional Resources
https://www.khronos.org/opengl/wiki/Shader_Compilation 

#Adding Projection
Projection is just a matrix that needs to be multiplied with in the shader.