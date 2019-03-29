package com.yun.jvm.chaper1;

import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project: zookeeperdemo
 * @Description:
 * @Author: wudr
 * @Date: 2019/3/8 15:17
 */
public class Metaspace extends ClassLoader {

    public static List<Class<?>> createClasser(){
        List<Class<?>> classes = new ArrayList<>();
        for (int i=0;i<10000000;i++){
            ClassWriter cw = new ClassWriter(0);
            cw.visit(Opcodes.V1_1,Opcodes.ACC_PUBLIC,"Class"+i,null,"java/lang/Object",null);
            MethodVisitor methodVisitor = cw.visitMethod(Opcodes.ACC_PUBLIC,"<init>","()V",null,null);
            methodVisitor.visitVarInsn(Opcodes.ALOAD,0);
            methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL,"java/lang/Object",
                    "<init>", "()V");
            methodVisitor.visitInsn(Opcodes.RETURN);
            methodVisitor.visitMaxs(1,1);
            methodVisitor.visitEnd();
            Metaspace test = new Metaspace();
            byte[] code = cw.toByteArray();
            Class<?> aClass = test.defineClass("Class" + i, code, 0, code.length);
            classes.add(aClass);
        }
        return classes;
    }
}
