package com.hy.annotation_handle;

import com.hy.annotation.BindView;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

public class BindingProcessor extends AbstractProcessor {

    Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.println("Binding Processor runing执行" + roundEnvironment.getRootElements().size());
        for (Element element : roundEnvironment.getRootElements()) {
            System.out.println("Binding Processor runing执行" + element.getSimpleName());
            System.out.println("Binding Processor runing执行***" + element.getEnclosingElement().toString());
            String packageNameStr = element.getEnclosingElement().toString();
            String classNameStr = element.getSimpleName().toString();

            ClassName targetClassName = ClassName.get(packageNameStr,classNameStr+"$Binding");

            MethodSpec.Builder methodBuilder = MethodSpec.constructorBuilder()
                                                .addModifiers(Modifier.PUBLIC)
                                                .addParameter(ClassName.get(packageNameStr,classNameStr),"activity");

            boolean isUseBindView = false;

            for (Element enclosedElement : element.getEnclosedElements()){
                if (enclosedElement.getKind() == ElementKind.FIELD){
                    BindView bindView = enclosedElement.getAnnotation(BindView.class);
                    if (bindView != null){
                        isUseBindView = true;
                        methodBuilder.addStatement("activity.$N = activity.findViewById($L)",
                                enclosedElement.getSimpleName(),bindView.value());
                    }
                }
            }

            TypeSpec typeSpec = TypeSpec.classBuilder(targetClassName)
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(methodBuilder.build())
                    .build();

            if (isUseBindView){
                try {
                    JavaFile.builder(targetClassName.packageName(),typeSpec)
                            .build()
                            .writeTo(filer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(BindView.class.getCanonicalName());
    }
}
