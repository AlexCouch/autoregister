# AutoRegistry Framework for MC Mod Addons!
-------------------------------------------
This framework gives you, a mod developer, more control over your addon registry system without breaking addons!

## Implementation
First step is to get it into your libs. Second step is to understand how it works. In summary, there are two phases of the execution of this framework: Population, Registration. But first, let's install it.

### Installation
First, you need the jar inside of your 'libs' folder in your root directory. You can get the jar from curseforge or from 'releases' here on the Github repo. Once you have that, you're gonna need to refresh your gradle project. You can do that by either opening the Gradle window in Intellij or by opening your terminal/cmd and type in `gradlew --refresh-dependencies setupDecompWorkspace`. It should now be viewable in your project. (P.S.: Intellij is better to use. Efficient, slick, and easier to use.) Now it's in your project. Make sure to do your dependencies right in your main class.

Second, you need to understand how it works. It's pretty simple. There's two phases to it; and there's 3 parts that you need for this to work. The 3 parts are: the annotation you want to use, the supertype for all the types being registered, and lastly the "registry item", which is a type that contains the registered type and it's registry information (I'll get to that in a bit).

First off, what on earth is a registry item? Well, a registry item is a class or interface that you're going to create to transport the information in your annotation to your registry system from the populate phase to the registry phase. So let's go ahead and create one.

The example I'm going to use is for the MrCrayfish Device Mod Applications API. I'm creating an interface which can be seen here:
<br>![Registry Item Interface](https://i.imgur.com/jgcn8lf.png)

The `RegistryItem<T>` interface takes a type parameter of whatever type you are containing. This is not the same as the type your are registering. This is the type that is going to be used to register the type. So in this case, the `Class<? extends Application>` is what is used for registering applications. So that will be `T`.

Next, we need to create the plugin. This plugin is a class that implements `Registry<T : RegistryItem<*>, R, S>` (which is Kotlin for `Registry<T extends RegistryItem<?>, R, S>`). `T` is the registry item we created. R is the type being registered, so in my case I'll do `Application`; lastly, S, is the Annotation, which is nonexistent. So I'm going to create one called `CDMRegister`, cause I would like to use it for Task as well. Now we pass in `CDMRegister` for `S` and we're good to go. You'll need to implement the abstract members. If you're in java, you should get 5 methods to implement.

`getAnnotationName` just needs to be the canonical name of your annotation. In my case, it'll be `CDMRegister.class.getCanonicalName()`.

<br>`getRequiredSuperclass` is the class that you want to check all the annotated classes against. It has to be `Class<R>`, so for me, `Class<Application>`. I'm gonna need to return `Application.class`.

<br>`getRegistryId` could be temporary. I'm including it in just in case it's need in the future, but if I see no fit for it in the future, I'll remove it. This is just a resource location for identificataion (obviously).

<br>`addItem` is your implementation of the first phase. This method is called when an item is being added to the registry set. You're provided the annotation used and the class being annotated. Now you need to take the information in your annotation (with or without the class, it's there if you need it) into a registry item of type T, then add it to the registry set.
<br>![Add Item Image](https://i.imgur.com/RChGqzv.png)
<br>This is my implementation of it for apps. I've created an extra method for creating an IAppContainer instance.
<br>![New App Container Image](https://i.imgur.com/K9TJNTk.png)
<br>Now we need to implement `registerItems` which just iterates through the registry set and passes them into the actual registry system.
<br>![Register Items Image](https://i.imgur.com/ho3Tzms.png)
<br>Now we have everything ready! We just need to register our plugin. It needs to go into preInit. You need to call
<br>Kotlin: `RegistryManager.addRegistry(PluginClass.class)`
<br>Java: `RegistryManager.INSTANCE.addRegistry(PluginClass.class)`
<br>Now your addons can register automatically, while giving you completely control of the process without affecting addons!
