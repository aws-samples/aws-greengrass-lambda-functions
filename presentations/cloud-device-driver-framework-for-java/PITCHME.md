### Cloud Device Driver (CDD) framework for Java

---

@snap[north]
#### CDD in one sentence
@snapend

The CDD framework streamlines the development process for Greengrass Lambda functions in Java by providing common convenience methods and well-tested coding patterns

---

How to create a CDD based Java application

+++

@snap[north]
#### Step 1
@snapend

Create a class called "App" that implements the "BaselineApp" interface

+++

@snap[north]
#### Step 1 (continued)
@snapend

Example, baseline

```java
public class App implements BaselineApp {
    private static final
    AppInjector appInjector = DaggerAppInjector.create();

    static {
        new App().initialize();
    }

    // Greengrass requires a no-args constructor, do not remove
    public App() {
    }

    @Override
    public BaselineInjector getBaselineInjector() {
        return appInjector;
    }
}
```

+++

@snap[north]
#### Step 2
@snapend

Override getStartupHandlers() method to hook into any classes that handle startup events

```java
    @Override
    public Set<GreengrassStartEventHandler> getStartupHandlers() {
        return new HashSet<>
                (Arrays.asList(appInjector.startupHandler()));
    }
```

+++

@snap[north]
#### Step 3
@snapend

Override getLambdaHandlers() method to hook into any classes that handle messages or can be invoked by another Lambda

```java
    @Override
    public Set<GreengrassLambdaEventHandler> getLambdaHandlers() {
        return new HashSet<>
                (Arrays.asList(appInjector.requestHandler()));
    }
```

+++

@snap[north]
#### Step 4
@snapend

Create an AppInjector interface that extends BaselineInjector and can inject the startup and input handler classes.

@snap[south text-05]
Note: AppModule.class is an optional Dagger module that can provide classes that need additional configuration
@snapend

```java
@Singleton
@Component(modules = {BaselineAppModule.class, AppModule.class})
public interface AppInjector extends BaselineInjector {
    StartupHandler startupHandler();

    RequestHandler requestHandler();

    // This class receives a custom event
    MyEventHandler myEventHandler();
}
```

---

#### Want to process custom events?

+++

@snap[north]
#### Step 1
@snapend

#### Add the classes that receive the events to the injector (AppInjector.java)

```java
@Singleton
@Component(modules = {BaselineAppModule.class, AppModule.class})
public interface AppInjector extends BaselineInjector {
    StartupHandler startupHandler();

    RequestHandler requestHandler();

    // This class receives a custom event
    MyEventHandler myEventHandler();
}
```

@snap[south text-05]
Note: AppModule.class is an optional Dagger module that can provide classes that need additional configuration
@snapend

+++

@snap[north]
#### Step 2
@snapend

#### Instantiate the classes in your App statically (App.java)

```java
    private static final
    AppInjector appInjector = DaggerAppInjector.create();
    private static final
    MyEventHandler myEventHandler = appInjector.myEventHandler();
```

+++

@snap[north]
#### Step 3
@snapend

#### Create a handler in the class that receives the event (MyEventHandler.java)

```java
    public void myEvent(MyEvent myEvent) {
      ...
    }
```

+++

@snap[north]
#### Step 4
@snapend

#### Inject the dispatcher into the class that receives the event (MyEventHandler.java)

```java
    @Inject
    Dispatcher dispatcher;

    // Empty, @Inject annotated constructor is required
    @Inject
    public MyEventHandler() {
    }
```
+++

@snap[north]
#### Step 5
@snapend

#### Add an afterInject method that tells the dispatcher the event class and the method that receives it (MyEventHandler.java)

```java
    @Inject
    public void afterInject() {
        dispatcher.add(ImmutableMyEvent.class, this::myEvent);
    }
```
