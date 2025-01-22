## SOBRE MI APP ##

La aplicación desarrollada en este proyecto se trata de una aplicación para llevar el control de los Pokemon capturados, así como una breve información de ellos. Se puede usar como complemento a otros juegos de Pokemon. 

Por ahora, solo contiene a los pokemon de la primera generación(151). Los datos de los Pokemon se han obtenido desde la API https://pokeapi.co/

### CARACTERÍSTICAS PRINCIPALES ###

La aplicación cuenta con un proceso de autenticación, mediante el que puedes registrarte para guardar tus pokemon capturados en la base de datos y recuperarlos desde cualquier dispositivo. Se puede registrar con email y con Google.

Una vez iniciada la sesión, podemos ver un menú de navegación en la parte baja desde el que podemos alternar 3 pantallas:

- *Mis Pokemon*: aquí se mostrarán los pokemon que hayas seleccionado para capturar desde la pantalla de Pokedex. Nos muestra su imagen, su nombre y sus tipos.
  
Si tenemos la opción activada, podemos eliminar Pokemon capturados deslizandolos hacia la izquierda. Si pulsamos sobre alguno de los Pokemon, se nos mostrará una pantalla información sobre él.

<img src="https://github.com/user-attachments/assets/595fbfb9-6c62-4b2d-8df3-fd4589196e03" width="33%" height="33%">

<img src="https://github.com/user-attachments/assets/13f61769-9ee6-4729-ad2e-3042d1f28f44" width="33%" height="33%">


- *Pokedex*: en esta pantalla se encuentran listados los 151 Pokemons. Al seleccionarlos, se guardarán como capturados y aparecerán marcados en oscuro:
  
  <img src="https://github.com/user-attachments/assets/38871de1-8c25-42c2-bbb5-f2960c1b7a62" width="33%" height="33%">
  
- *Ajustes*: podemos modificar algunas preferencias como el idioma o la opción de eliminar Pokemon capturados. La opción "Acerca de" muestra información sobre la aplicación.
  
<img src="https://github.com/user-attachments/assets/5219e28c-16dd-468f-a78f-42943a587e23" width="33%" height="33%">


### TECNOLOGÍAS UTILIZADAS ###

- *Retrofit*: herramienta para obtener información desde APIs externas a través de internet. Usada para obtener la información de los Pokemon.
- *Firebase Authentication*: para el proceso de autenticación de usuario en la aplicación.
- *Firebase Store*: com sistema de base de datos de la aplicación. Guarda los Pokemon capturados de cada usuario.
- *Recycler View*: tipo de View utilizado para listar los Pokemon, tanto de la Pokedex como los capturados.
- *Picasso*: utilizado para mostrar las imágenes de los Pokemon desde una URL
- *Navigation Fragment*: para el control de fragmentos dentro de la actividad principal

### DEPENDENCIAS NECESARIAS ###
~~~
''
//Recycler View
implementation("androidx.recyclerview:recyclerview:1.3.2")
implementation("androidx.cardview:cardview:1.0.0")


//Navigation
implementation("androidx.navigation:navigation-ui:2.8.5")
implementation("androidx.navigation:navigation-fragment:2.8.5")

//Firebase
implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
implementation("com.google.firebase:firebase-analytics")
implementation("com.firebaseui:firebase-ui-auth:7.2.0")
implementation("com.google.firebase:firebase-firestore")

//Retrofit
implementation("com.google.code.gson:gson:2.7")
implementation("com.squareup.retrofit2:retrofit:2.1.0")
implementation("com.squareup.retrofit2:converter-gson:2.1.0")


//Picasso
implementation("com.squareup.picasso:picasso:2.5.0")
''
~~~

### CÓMO CLONAR MI PROYECTO ###
- 1º Pulsa el botón "<>CODE" y copia el URL en la modalidad que te interese
- 2º Abre Git Bash y posicionate en el directorio donde te interese clonar el poyecto
- 3º Escribe el comando ''git clone'' seguido de la URL que has copiado en el primer paso. Pulsar "Enter" y se creará el clon del proyecto.

### CONCLUSIONES DE LA DESARROLLADORA SOBRE EL PROYECTO ###

Este proyecto ha sido realizado con mucha ilusión ya que su temática es de una de mis sagas de juegos favorita. He intentado darle un estilo que se asemejara a una Pokedex Vintage, con pantalla verde.

Me ha resultado un proyecto muy completo en el que he aprendido el uso de varias herramientas y funciones que no había visto hasta ahora como Retrofit, Firebase o cómo usar la función de deslizamiento. 
El aprendizaje de estas nuevas herramientas me ha consumido un tiempo considerable, por lo que no le he dedicado tanto tiempo como me hubiera gustado a la estética de la aplicación. En un futuro me gustaría 
seguir trabajando sobre ella.

Quizás, lo que me ha llevado más tiempo ha sido el manejo de la base de datos, que funciona de manera asíncrona y, en varias ocasiones, no conseguía los resultados que esperaba. 

En conclusión, he disfrutado el proceso de desarrollo de esta aplicación ya que mezcla dos mundos que me gustan mucho: los videojuegos y la programación.
