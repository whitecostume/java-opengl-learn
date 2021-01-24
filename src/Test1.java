import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import org.lwjgl.opengl.GL33;

import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.system.MemoryUtil.NULL;

public class Test1 {
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


        while (!GLFW.glfwWindowShouldClose(window)){

            if (GLFW.glfwGetKey(window,GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS){
               GLFW.glfwSetWindowShouldClose(window,true);
            }
            GL33.glClearColor(0,0,0,0);
            GL33.glClear(GL33.GL_COLOR_BUFFER_BIT);

            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
        }

        GLFW.glfwTerminate();

    }
}
