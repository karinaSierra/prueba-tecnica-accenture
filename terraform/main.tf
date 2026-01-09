terraform {
  required_version = ">= 1.0"
  
  required_providers {
    docker = {
      source  = "kreuzwerker/docker"
      version = "~> 3.0"
    }
  }
}

provider "docker" {}

# Red Docker para la aplicación
resource "docker_network" "franquicias_network" {
  name = "franquicias-network"
}

# Volumen para persistencia de MySQL
resource "docker_volume" "mysql_data" {
  name = "franquicias-mysql-data"
}

# Contenedor MySQL
resource "docker_image" "mysql" {
  name = "mysql:8.0"
}

resource "docker_container" "mysql" {
  name  = "franquicias-mysql"
  image = docker_image.mysql.image_id
  
  env = [
    "MYSQL_ROOT_PASSWORD=root_password",
    "MYSQL_DATABASE=franquicias_db",
    "MYSQL_USER=franquicias_user",
    "MYSQL_PASSWORD=franquicias_pass"
  ]
  
  ports {
    internal = 3306
    external = 3306
  }
  
  volumes {
    volume_name    = docker_volume.mysql_data.name
    container_path = "/var/lib/mysql"
  }
  
  networks_advanced {
    name = docker_network.franquicias_network.name
  }
  
  healthcheck {
    test     = ["CMD", "mysqladmin", "ping", "-h", "localhost", "-u", "root", "-proot_password"]
    interval = "10s"
    timeout  = "5s"
    retries  = 5
  }
}

# Imagen de la aplicación (se construye localmente)
resource "docker_image" "app" {
  name = "franquicias-api:latest"
  
  build {
    context    = ".."
    dockerfile = "Dockerfile"
  }
}

# Contenedor de la aplicación
resource "docker_container" "app" {
  name  = "franquicias-api"
  image = docker_image.app.image_id
  
  env = [
    "SPRING_R2DBC_URL=r2dbc:mysql://mysql:3306/franquicias_db?useSSL=false&allowPublicKeyRetrieval=true",
    "SPRING_R2DBC_USERNAME=franquicias_user",
    "SPRING_R2DBC_PASSWORD=franquicias_pass",
    "DB_USERNAME=franquicias_user",
    "DB_PASSWORD=franquicias_pass"
  ]
  
  ports {
    internal = 8080
    external = 8080
  }
  
  networks_advanced {
    name = docker_network.franquicias_network.name
  }
  
  depends_on = [docker_container.mysql]
  
  restart = "unless-stopped"
}

