<?php

namespace Database\Factories;

use Illuminate\Database\Eloquent\Factories\Factory;

class AlgorithmConfigFactory extends Factory
{
    /**
     * Define the model's default state.
     */
    public function definition(): array
    {
        return [
            'adjustment_factor' => fake()->randomFloat(0, 0, 9999999999.),
            'clamp_min' => fake()->randomFloat(0, 0, 9999999999.),
            'clamp_max' => fake()->randomFloat(0, 0, 9999999999.),
            'cutoff_score' => fake()->randomFloat(0, 0, 9999999999.),
            'list_size' => fake()->numberBetween(-10000, 10000),
            'era_weight' => fake()->randomFloat(0, 0, 9999999999.),
            'director_weight' => fake()->randomFloat(0, 0, 9999999999.),
            'genre_weight' => fake()->randomFloat(0, 0, 9999999999.),
            'duration_weight' => fake()->randomFloat(0, 0, 9999999999.),
        ];
    }
}
