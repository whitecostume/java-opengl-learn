import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL33;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.util.logging.Logger;

import static org.lwjgl.system.MemoryUtil.NULL;

public class Test3 {

    public static String vertexShaderSource =
            "#version 330 core\n"+
                    "out vec4 outColor;\n"+
                    "out vec2 texcoord;\n"+
                    "layout (location = 0) in vec3 aPos;\n"+
                    "layout (location = 1) in vec3 aColor;\n"+
                    "layout (location = 2) in vec2 aTexCoord;\n"+
                    "void main()\n"+
                    "{\n"+
                    "   gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);\n"+
                    "   outColor = vec4(aColor,1.0f);\n"+
                    "   texcoord = aTexCoord;\n"+
                    "}\0";

    public static String fragmentShaderSource =
            "#version 330 core\n"+
                    "in vec4 outColor;\n"+
                    "in vec2 texcoord;\n"+
                    "out vec4 FragColor;\n"+
                    "uniform sampler2D texture;\n"+
                    "uniform sampler2D texture1;\n"+
                    "void main()\n"+
                    "{\n"+
                    "   FragColor = mix (texture(texture,texcoord) ,texture(texture1,texcoord),0.2);\n"+
                    "}\n\0";

    public static void main(String[] args) {
        GLFWErrorCallback.createPrint(System.err).set();

        GLFW.glfwInit();

        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR,3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR,3);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE,GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT,GLFW.GLFW_TRUE);

        long window = GLFW.glfwCreateWindow(800,600,"Test1", MemoryUtil.NULL,MemoryUtil.NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");



        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwShowWindow(window);

        GL.createCapabilities();

        GLFW.glfwSetFramebufferSizeCallback(window,(window1, width, height) -> {
            GL33.glViewport(0,0,width,height);
        });

//        开始编译shader
        int vertexShader = GL33.glCreateShader(GL33.GL_VERTEX_SHADER);
        GL33.glShaderSource(vertexShader,vertexShaderSource);
        GL33.glCompileShader(vertexShader);

        int[] success =new int[1];

        GL33.glGetShaderiv(vertexShader,GL33.GL_COMPILE_STATUS,success);
        if (success[0] == GLFW.GLFW_FALSE){
            String infoLog = GL33.glGetShaderInfoLog(vertexShader);
            throw new RuntimeException("Failed Compile vertexShader " + infoLog);

        }


        int fragemtShader = GL33.glCreateShader(GL33.GL_FRAGMENT_SHADER);
        GL33.glShaderSource(fragemtShader,fragmentShaderSource);
        GL33.glCompileShader(fragemtShader);


        GL33.glGetShaderiv(fragemtShader,GL33.GL_COMPILE_STATUS,success);
        if (success[0] == GLFW.GLFW_FALSE){
            String infoLog = GL33.glGetShaderInfoLog(fragemtShader);
            throw new RuntimeException("Failed Compile vertexShader " + infoLog);

        }

        int shaderProgram = GL33.glCreateProgram();
        GL33.glAttachShader(shaderProgram,vertexShader);
        GL33.glAttachShader(shaderProgram,fragemtShader);
        GL33.glLinkProgram(shaderProgram);

        GL33.glGetProgramiv(shaderProgram,GL33.GL_LINK_STATUS,success);
        if (success[0] == GLFW.GLFW_FALSE){
            String infoLog = GL33.glGetProgramInfoLog(shaderProgram);
            throw new RuntimeException("Failed Compile vertexShader " + infoLog);

        }

        GL33.glDeleteShader(vertexShader);
        GL33.glDeleteShader(fragemtShader);

//        创建纹理
        int[] texture = new int[1];
        GL33.glGenTextures(texture);
        GL33.glBindTexture(GL33.GL_TEXTURE_2D,texture[0]);
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D,GL33.GL_TEXTURE_WRAP_S, GL33.GL_REPEAT);
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D,GL33.GL_TEXTURE_WRAP_T, GL33.GL_REPEAT);
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D,GL33.GL_TEXTURE_MIN_FILTER,GL33.GL_LINEAR);
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D,GL33.GL_TEXTURE_MAG_FILTER,GL33.GL_LINEAR);


        int[] width = new int[1];
        int[] hight = new int[1];
        int[] channel = new int[1];

        STBImage.stbi_set_flip_vertically_on_load(true);
        ByteBuffer byteBuffer = STBImage.stbi_load("/Users/white/IdeaProjects/untitled1/assets/badlogic.jpg",
                width,hight,channel,0);


        GL33.glTexImage2D(GL33.GL_TEXTURE_2D,0,GL33.GL_RGB,256,256,0,GL33.GL_RGB,GL33.GL_UNSIGNED_BYTE,byteBuffer);
        GL33.glGenerateMipmap(texture[0]);

        STBImage.stbi_image_free(byteBuffer);

        int[] texture1 = new int[1];
        GL33.glGenTextures(texture1);
        GL33.glBindTexture(GL33.GL_TEXTURE_2D,texture1[0]);
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D,GL33.GL_TEXTURE_WRAP_S, GL33.GL_REPEAT);
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D,GL33.GL_TEXTURE_WRAP_T, GL33.GL_REPEAT);
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D,GL33.GL_TEXTURE_MIN_FILTER,GL33.GL_LINEAR);
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D,GL33.GL_TEXTURE_MAG_FILTER,GL33.GL_LINEAR);

        byteBuffer = STBImage.stbi_load("/Users/white/IdeaProjects/untitled1/assets/awesomeface.png",
                width,hight,channel,0);


        GL33.glTexImage2D(GL33.GL_TEXTURE_2D,0,GL33.GL_RGBA,width[0],hight[0],0,GL33.GL_RGBA,GL33.GL_UNSIGNED_BYTE,byteBuffer);
        GL33.glGenerateMipmap(texture1[0]);

        STBImage.stbi_image_free(byteBuffer);


        float vertices[] = {
                0.5f, 0.5f, 0.0f,    1.0f,0.0f,0.0f,  1.0f,1.0f,// 右上角
                0.5f, -0.5f, 0.0f,   0.0f,1.0f,0.0f,  1.0f,0.0f,// 右下角
                -0.5f, -0.5f, 0.0f,  0.0f,0.0f,1.0f,  0.0f,0.0f,// 左下角
                -0.5f, 0.5f, 0.0f ,  1.0f,0.0f,0.0f,  0.0f,1.0f// 左上角
        };

        int indices[] = { // 注意索引从0开始!
                0, 1, 3, // 第一个三角形
                1, 2, 3  // 第二个三角形
        };


        int[] VAO = new int[1];
        int[] VBO = new int[1];
        int[] EBO = new int[1];

        GL33.glGenVertexArrays(VAO);

        GL33.glGenBuffers(VBO);
        GL33.glGenBuffers(EBO);
        GL33.glBindVertexArray(VAO[0]);


        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER,VBO[0]);
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER,vertices,GL33.GL_STATIC_DRAW);

        GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER,EBO[0]);
        GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER,indices,GL33.GL_STATIC_DRAW);

        GL33.glVertexAttribPointer(0,3,GL33.GL_FLOAT,false,8*Float.BYTES,0);
        GL33.glEnableVertexAttribArray(0);
        GL33.glVertexAttribPointer(1,3,GL33.GL_FLOAT,false,8*Float.BYTES,3*Float.BYTES);
        GL33.glEnableVertexAttribArray(1);
        GL33.glVertexAttribPointer(2,2,GL33.GL_FLOAT,false,8*Float.BYTES,6*Float.BYTES);
        GL33.glEnableVertexAttribArray(2);

        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER,0);
        GL33.glBindVertexArray(0);






        GL33.glUseProgram(shaderProgram);
        GL33.glUniform1i(GL33.glGetUniformLocation(shaderProgram,"texture"),0);
        GL33.glUniform1i(GL33.glGetUniformLocation(shaderProgram,"texture1"),1);




        while (!GLFW.glfwWindowShouldClose(window)){

            if (GLFW.glfwGetKey(window,GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS){
                GLFW.glfwSetWindowShouldClose(window,true);
            }
            GL33.glClearColor(0,0,0,0);
            GL33.glClear(GL33.GL_COLOR_BUFFER_BIT);





            GL33.glUseProgram(shaderProgram);

            GL33.glActiveTexture(GL33.GL_TEXTURE0);
            GL33.glBindTexture(GL33.GL_TEXTURE_2D,texture[0]);
            GL33.glActiveTexture(GL33.GL_TEXTURE1);
            GL33.glBindTexture(GL33.GL_TEXTURE_2D,texture1[0]);

            GL33.glBindVertexArray(VAO[0]);
            GL33.glDrawElements(GL33.GL_TRIANGLES,6,GL33.GL_UNSIGNED_INT,0);

            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();


        }

        GLFW.glfwTerminate();

    }
}
