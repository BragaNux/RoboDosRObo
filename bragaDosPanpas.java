package BrayanMartins;

import robocode.*;
import java.awt.Color;

//herança: BrayanMartins agora herda de AdvancedRobot para maior controle
public class BrayanMartins extends AdvancedRobot {
    private RobotStrategy strategy;

    // principal - comportamento do robo (responsabilidade unica - SRP)
    @Override
    public void run() {
        setBodyColor(Color.black);
        setGunColor(Color.red);
        setRadarColor(Color.black);

        // define uma estrategia padrão
        strategy = new AggressiveStrategy();

        // loop
        while (true) {
            // executa a estrategia atual
            strategy.execute(this);
        }
    }

    // detecta outro robo (aberto/fechado - OCP)
    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        double distancia = e.getDistance();
        double energiaInimigo = e.getEnergy();

        // muda estratégia baseado no nome do oponente (Polimorfismo)
        if (e.getName().contains("Walls")) {
            strategy = new CheiraParedeStrategy();
        } else if (e.getName().contains("SpinBot")) {
            strategy = new BlabladeStrategy();
        } else {
            strategy = new DefaultStrategy();
        }

        // estrategia atual
        strategy.onScannedRobot(this, e, distancia, energiaInimigo);
    }

    // recebe tiro e faz munwalk (substituicao de liskov sei la doq - LSP)
    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        if (getEnergy() > 30) {
            turnLeft(90); // munwalk pra confundir
            ahead(50); // munwalk pra desviar
        } else {
            // ta fraco? movimento mais radical
            turnRight(180); // manda aquele giro inesperado
            ahead(100); // foge com elegancia
        }
    }

    // cheira parede (segregacao de interface - ISP)
    @Override
    public void onHitWall(HitWallEvent e) {
        if (getEnergy() > 50) {
            back(50); // viu parede
            turnRight(90); // sai pra nao ficar preso
        } else {
            // energia baixa maior evasao
            back(100);
            turnLeft(90);
        }
    }
}

// interface para estrategias (inversao de dependencia - DIP)
interface RobotStrategy {
    void execute(AdvancedRobot robot);

    default void onScannedRobot(AdvancedRobot robot, ScannedRobotEvent e, double distancia, double energiaInimigo) {
        // Default: Faz nada
    }
}

// estrategia agressiva (padrao de projeto estrategia)
class AggressiveStrategy implements RobotStrategy {
    @Override
    public void execute(AdvancedRobot robot) {
        robot.ahead(150); // avança mais pra pressão
        robot.turnGunRight(360); // procura inimigos
        robot.back(100); // recua pra enganar
        robot.turnGunRight(360); // procura dnv
    }
}

// estrategia contra CheiraParede
class CheiraParedeStrategy implements RobotStrategy {
    @Override
    public void execute(AdvancedRobot robot) {
        robot.fire(3); // forca maxima porque ele so cheira parede mesmo
        robot.turnRight(90); // sai do caminho do cheirador
        robot.ahead(200); // ganha distancia porque parede nao atira
    }

    @Override
    public void onScannedRobot(AdvancedRobot robot, ScannedRobotEvent e, double distancia, double energiaInimigo) {
        execute(robot);
    }
}

// Estratégia contra Blablade
class BlabladeStrategy implements RobotStrategy {
    @Override
    public void execute(AdvancedRobot robot) {
        robot.turnGunRight(360); // fica girando pra acompanhar o giro dele
        robot.fire(2); // da tiros frequentes pra quebrar o ciclo do giro
        robot.back(100); // mantem distancia pra evitar colisoes
    }

    @Override
    public void onScannedRobot(AdvancedRobot robot, ScannedRobotEvent e, double distancia, double energiaInimigo) {
        execute(robot);
    }
}

// Estratégia padrão
class DefaultStrategy implements RobotStrategy {
    @Override
    public void onScannedRobot(AdvancedRobot robot, ScannedRobotEvent e, double distancia, double energiaInimigo) {
        if (distancia < 50) {
            robot.fire(3); // close-range massacre!
        } else if (distancia < 200) {
            robot.fire(2); // medium-range ataque
        } else {
            robot.fire(1); // longa distância atira uma vez
        }

        if (energiaInimigo < 20) {
            robot.fire(3); // finaliza com estilo
            robot.turnRight(30);
            robot.ahead(50); // se aproxima pra garantir a vitoria
        }
    }
}
