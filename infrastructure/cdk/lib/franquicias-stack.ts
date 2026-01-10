import * as cdk from 'aws-cdk-lib';
import * as ec2 from 'aws-cdk-lib/aws-ec2';
import * as iam from 'aws-cdk-lib/aws-iam';
import * as logs from 'aws-cdk-lib/aws-logs';
import { Construct } from 'constructs';
import * as rds from 'aws-cdk-lib/aws-rds';


export interface FranquiciasApiStackProps extends cdk.StackProps {
  /**
   * EC2 Instance Type
   */
  instanceType?: ec2.InstanceType;
  
  /**
   * Application configuration
   */
  appName?: string;
  environment?: string;
}

export class FranquiciasApiStack extends cdk.Stack {
  public readonly vpc: ec2.Vpc;
  public readonly ec2Instance: ec2.Instance;

  constructor(scope: Construct, id: string, props?: FranquiciasApiStackProps) {
    super(scope, id, props);

    const appName = props?.appName || 'franquicias-api';
    const environment = props?.environment || 'dev';

    // ============================================
    // 1. VPC Configuration
    // ============================================
    this.vpc = new ec2.Vpc(this, 'VPC', {
      maxAzs: 2,
      natGateways: 0, // Sin NAT Gateway para reducir costos (EC2 en subnets públicas)
      subnetConfiguration: [
        {
          cidrMask: 24,
          name: 'Public',
          subnetType: ec2.SubnetType.PUBLIC,
        },
      ],
      enableDnsHostnames: true,
      enableDnsSupport: true,
    });

    // ============================================
    // 2. Security Group para EC2
    // ============================================
    const ec2SecurityGroup = new ec2.SecurityGroup(this, 'Ec2SecurityGroup', {
      vpc: this.vpc,
      description: 'Security group for EC2 instance - API de Franquicias',
      allowAllOutbound: true,
    });

    // Permitir tráfico HTTP en puerto 8080 desde cualquier IP
    ec2SecurityGroup.addIngressRule(
      ec2.Peer.anyIpv4(),
      ec2.Port.tcp(8080),
      'Allow HTTP traffic on port 8080'
    );

    // Permitir SSH (opcional, si necesitas acceso)
    ec2SecurityGroup.addIngressRule(
      ec2.Peer.anyIpv4(),
      ec2.Port.tcp(22),
      'Allow SSH access'
    );


    // ============================================
    // 3. Security Group para RDS
    // ============================================
    const rdsSecurityGroup = new ec2.SecurityGroup(this, 'RdsSecurityGroup', {
      vpc: this.vpc,
      description: 'Security group for RDS MySQL',
      allowAllOutbound: true,
    });
    
    // Permitir conexión SOLO desde la EC2
    rdsSecurityGroup.addIngressRule(
      ec2SecurityGroup,
      ec2.Port.tcp(3306),
      'Allow MySQL access from EC2'
    );

    // ============================================
    // 3. IAM Role para EC2
    // ============================================
    const ec2Role = new iam.Role(this, 'Ec2Role', {
      assumedBy: new iam.ServicePrincipal('ec2.amazonaws.com'),
      description: 'Role for EC2 instance to access AWS services',
      managedPolicies: [
        iam.ManagedPolicy.fromAwsManagedPolicyName('CloudWatchAgentServerPolicy'),
        iam.ManagedPolicy.fromAwsManagedPolicyName('AmazonSSMManagedInstanceCore'),
      ],
    });


    // ============================================
    // 4. CloudWatch Log Group
    // ============================================
    const logGroup = new logs.LogGroup(this, 'LogGroup', {
      logGroupName: `/ec2/${appName}`,
      retention: logs.RetentionDays.ONE_WEEK,
      removalPolicy: cdk.RemovalPolicy.DESTROY,
    });

    // Permitir a EC2 escribir logs
    logGroup.grantWrite(ec2Role);

    // ============================================
    // 4. RDS MySQL
    // ============================================
    const database = new rds.DatabaseInstance(this, 'FranquiciasDatabase', {
      engine: rds.DatabaseInstanceEngine.mysql({
        version: rds.MysqlEngineVersion.VER_8_0,
      }),
      instanceType: ec2.InstanceType.of(
        ec2.InstanceClass.T3,
        ec2.InstanceSize.MICRO
      ),
      vpc: this.vpc,
      vpcSubnets: {
        subnetType: ec2.SubnetType.PUBLIC,
      },
      securityGroups: [rdsSecurityGroup],
      publiclyAccessible: true,
      allocatedStorage: 20,
      credentials: rds.Credentials.fromPassword(
        'admin',
        cdk.SecretValue.unsafePlainText('Franquicias123!')
      ),
      databaseName: 'franquicias_db',
      removalPolicy: cdk.RemovalPolicy.DESTROY,
      deletionProtection: false,
    });
    

    // ============================================
    // 5. EC2 Instance
    // ============================================
    const userData = ec2.UserData.forLinux();
    
    // Script básico de User Data para instalar Java y configurar
    userData.addCommands(
      '#!/bin/bash',
      'yum update -y',
      'yum install -y java-21-amazon-corretto-headless docker',
      'systemctl start docker',
      'systemctl enable docker',
      'usermod -aG docker ec2-user',
    
      'mkdir -p /opt/franquicias-api',
    
      // Variables de entorno para Spring Boot
      `echo 'SPRING_R2DBC_URL=r2dbc:mysql://${database.dbInstanceEndpointAddress}:3306/franquicias_db' >> /etc/environment`,
      `echo 'SPRING_R2DBC_USERNAME=admin' >> /etc/environment`,
      `echo 'SPRING_R2DBC_PASSWORD=Franquicias123!' >> /etc/environment`,
      `echo 'SERVER_PORT=8080' >> /etc/environment`,
    
      // Cargar variables
      'source /etc/environment',
    
      // Levantar la app con Docker
      'docker run -d -p 8080:8080 ' +
        '--name franquicias-api ' +
        '-e SPRING_R2DBC_URL ' +
        '-e SPRING_R2DBC_USERNAME ' +
        '-e SPRING_R2DBC_PASSWORD ' +
        'ksierra33673/franquicias-api:latest'
    );
    


    // Crear instancia EC2
    this.ec2Instance = new ec2.Instance(this, 'Ec2Instance', {
      vpc: this.vpc,
      instanceType: props?.instanceType || ec2.InstanceType.of(ec2.InstanceClass.T3, ec2.InstanceSize.MICRO),
      machineImage: ec2.MachineImage.latestAmazonLinux2023({
        cpuType: ec2.AmazonLinuxCpuType.X86_64,
      }),
      vpcSubnets: {
        subnetType: ec2.SubnetType.PUBLIC,
      },
      securityGroup: ec2SecurityGroup,
      role: ec2Role,
      userData: userData,
      ssmSessionPermissions: true, // Habilitar acceso via Systems Manager Session Manager
    });

    // ============================================
    // 6. Outputs
    // ============================================
    new cdk.CfnOutput(this, 'Ec2InstanceId', {
      value: this.ec2Instance.instanceId,
      description: 'ID of the EC2 instance',
    });

    new cdk.CfnOutput(this, 'Ec2PublicIp', {
      value: this.ec2Instance.instancePublicIp,
      description: 'Public IP of the EC2 instance',
    });

    new cdk.CfnOutput(this, 'ApiUrl', {
      value: `http://${this.ec2Instance.instancePublicIp}:8080`,
      description: 'URL of the API',
    });
    new cdk.CfnOutput(this, 'DatabaseEndpoint', {
      value: database.dbInstanceEndpointAddress,
    });
    
  }
}
