package pl.j.szymekdo;

import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class AoC12 extends AoC {
    public static void main(String[] args) throws Exception {
        new AoC12(2);
    }

    AoC12() throws Exception {
        String[] input = readFile().split("\\r*\\n");
        ArrayList<Moon> moons = new ArrayList<>();
        for (String line : input) {
            moons.add(new Moon(line));
        }


        for (int i = 0; i < 1000; i++) {
            //System.out.println(i + " " + moons);
            for (int j = 0; j < moons.size(); j++) {
                Moon first = moons.get(j);
                for (int k = j; k < moons.size(); k++) {
                    Moon second = moons.get(k);
                    if (second.equals(first))
                        continue;
                    else {
                        second = first.calculateAndSetVelocities(second);
                        moons.set(k, second);
                        moons.set(j, first);
                    }
                }
            }

            for (Moon moon : moons) {
                moon.recalculatePosition();
            }


        }

        int totalEnergy = 0;

        for (Moon moon : moons) {
            totalEnergy += moon.getEnergy();
        }

        System.out.println(totalEnergy);


    }

    AoC12(int part2) throws Exception {
        String[] input = readFile().split("\\r*\\n");
        ArrayList<Moon> moons = new ArrayList<>();
        for (String line : input) {
            moons.add(new Moon(line));
        }

        final ArrayList<Moon> initialState = moons.stream().map(Moon::duplicate).collect(Collectors.toCollection(ArrayList::new));

        int xsteps = 0;
        int ysteps = 0;
        int zsteps = 0;

        int counter = 0;


        while (true) {
            for (int j = 0; j < moons.size(); j++) {
                Moon first = moons.get(j);
                for (int k = j; k < moons.size(); k++) {
                    Moon second = moons.get(k);
                    if (second.equals(first))
                        continue;
                    else {
                        second = first.calculateAndSetVelocities(second);
                        moons.set(k, second);
                        moons.set(j, first);
                    }
                }
            }

            for (Moon moon : moons) {
                moon.recalculatePosition();
            }


            counter++;

            if (xsteps==0 && moons.stream()
                     .map(e -> Pair.of(e.position.x, e.velocity.x))
                     .collect(Collectors.toList())
                     .containsAll(initialState.stream().map(e -> Pair.of(e.position.x, e.velocity.x)).collect(Collectors.toList()))) {
                xsteps = counter;
            }
            if (ysteps==0 && moons.stream()
                     .map(e -> Pair.of(e.position.y, e.velocity.y))
                     .collect(Collectors.toList())
                     .containsAll(initialState.stream().map(e -> Pair.of(e.position.y, e.velocity.y)).collect(Collectors.toList()))) {
                ysteps = counter;
            }
            if (zsteps==0 && moons.stream()
                     .map(e -> Pair.of(e.position.z, e.velocity.z))
                     .collect(Collectors.toList())
                     .containsAll(initialState.stream().map(e -> Pair.of(e.position.z, e.velocity.z)).collect(Collectors.toList()))) {
                zsteps = counter;
            }

            //counter++;

            if (xsteps > 0 && ysteps > 0 && zsteps > 0)
                break;
        }
        System.out.println(xsteps+" "+ysteps+" "+" "+zsteps+" "+counter);
        System.out.println(lcm(lcm(xsteps, ysteps), zsteps));

    }

    private static long lcm(long a, long b)
    {
        return a * (b / gcd(a, b));
    }

    private static long gcd(long a, long b)
    {
        while (b > 0)
        {
            long temp = b;
            b = a % b; // % is remainder
            a = temp;
        }
        return a;
    }

    private static class Moon {
        private String name;
        private Vector3d position;
        private Vector3d velocity;
        private int energy;

        private Moon(Moon second) {
            this.name = second.name;
            this.position = new Vector3d(second.position);
            this.velocity = new Vector3d(second.velocity);
            energy = 0;
        }

        Moon(String input) {
            this.name = (int) (Math.random() * 11 % 10) + "";
            this.position = parseInput(input);
            velocity = new Vector3d(0, 0, 0);
            energy = 0;
        }

        private Vector3d parseInput(String input) {
            String[] s = input.replaceAll("[<>]", "").split(",");
            int x = Integer.parseInt(s[0].trim().substring(2));
            int y = Integer.parseInt(s[1].trim().substring(2));
            int z = Integer.parseInt(s[2].trim().substring(2));
            return new Vector3d(x, y, z);

        }

        public void recalculatePosition() {
            this.position.add(velocity);
        }

        public Moon calculateAndSetVelocities(Moon second) {
            if (this.position.x > second.position.x) {
                this.velocity.x -= 1;
                second.velocity.x += 1;
            }
            else if (this.position.x < second.position.x) {
                this.velocity.x += 1;
                second.velocity.x -= 1;
            }

            if (this.position.y > second.position.y) {
                this.velocity.y -= 1;
                second.velocity.y += 1;
            }
            else if (this.position.y < second.position.y) {
                this.velocity.y += 1;
                second.velocity.y -= 1;
            }

            if (this.position.z > second.position.z) {
                this.velocity.z -= 1;
                second.velocity.z += 1;
            }
            else if (this.position.z < second.position.z) {
                this.velocity.z += 1;
                second.velocity.z -= 1;
            }

            return second;
        }

        public int calculateEnergy() {
            int potentialEnergy = (int) (Math.abs(position.x) + Math.abs(position.y) + Math.abs(position.z));
            int kineticEnergy = (int) (Math.abs(velocity.x) + Math.abs(velocity.y) + Math.abs(velocity.z));
            return energy = potentialEnergy * kineticEnergy;
        }

        public int getEnergy() {
            return calculateEnergy();
        }


        public Moon duplicate() {
            return new Moon(this);
        }

        @Override
        public String toString() {
            return "Moon{" +
                   "name='" + name + '\'' +
                   ", position=" + position +
                   ", velocity=" + velocity +
                   '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Moon moon = (Moon) o;
            return Objects.equals(name, moon.name) &&
                   Objects.equals(position, moon.position) &&
                   Objects.equals(velocity, moon.velocity);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, position, velocity);
        }
    }
}
