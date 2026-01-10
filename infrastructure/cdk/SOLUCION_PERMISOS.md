# Solución de Problemas de Permisos AWS

## ❌ Error de Permisos

Si ves este error:
```
User: arn:aws:iam::269805571389:user/cli-user is not authorized to perform: 
cloudformation:DescribeStacks on resource: arn:aws:cloudformation:us-east-1:269805571389:stack/CDKToolkit/*
```

Esto significa que tu usuario AWS (`cli-user`) no tiene los permisos necesarios para usar AWS CDK.

## ✅ Solución

### Opción 1: Agregar Permisos Mínimos de CDK (Recomendado)

El usuario IAM necesita los siguientes permisos:

**Política IAM mínima para CDK:**

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "cloudformation:*",
        "ssm:GetParameter",
        "ssm:GetParameters",
        "ssm:GetParametersByPath",
        "ssm:PutParameter",
        "s3:*",
        "iam:CreateRole",
        "iam:DeleteRole",
        "iam:GetRole",
        "iam:PassRole",
        "iam:AttachRolePolicy",
        "iam:DetachRolePolicy",
        "iam:PutRolePolicy",
        "iam:DeleteRolePolicy",
        "iam:GetRolePolicy",
        "iam:ListRolePolicies",
        "iam:ListAttachedRolePolicies",
        "sts:AssumeRole"
      ],
      "Resource": "*"
    }
  ]
}
```

### Opción 2: Usar Política Administradora (Solo para Desarrollo)

Para desarrollo/pruebas, puedes usar la política `AdministratorAccess`:

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": "*",
      "Resource": "*"
    }
  ]
}
```

⚠️ **ADVERTENCIA**: Esta política da acceso total. Úsala solo para entornos de desarrollo/pruebas.

### Pasos para Agregar Permisos:

1. **Desde la Consola de AWS:**
   - Ve a IAM → Users → `cli-user`
   - Click en "Add permissions" → "Attach policies directly"
   - Busca y selecciona `AdministratorAccess` (o crea una política personalizada)
   - Click en "Add permissions"

2. **Desde AWS CLI:**
   ```bash
   # Adjuntar política de administrador (solo para desarrollo)
   aws iam attach-user-policy \
     --user-name cli-user \
     --policy-arn arn:aws:iam::aws:policy/AdministratorAccess
   ```

3. **Esperar unos segundos** para que los permisos se propaguen

4. **Verificar permisos:**
   ```bash
   aws sts get-caller-identity
   ```

### Opción 3: Bootstrap CDK con Permisos Específicos

Si no puedes cambiar los permisos del usuario, puedes ejecutar bootstrap con un role específico:

```bash
cdk bootstrap --trust 269805571389 --cloudformation-execution-policies arn:aws:iam::aws:policy/AdministratorAccess
```

## 🔧 Verificación

Después de agregar los permisos, verifica que todo funciona:

```bash
# 1. Verificar credenciales
aws sts get-caller-identity

# 2. Verificar permisos de CloudFormation
aws cloudformation list-stacks --region us-east-1

# 3. Intentar bootstrap de nuevo
cdk bootstrap
```

## 📝 Notas Adicionales

- Los permisos pueden tardar unos minutos en propagarse
- Si sigues teniendo problemas, cierra sesión y vuelve a autenticarte:
  ```bash
  aws configure
  ```
- Para producción, usa el principio de menor privilegio y otorga solo los permisos necesarios

