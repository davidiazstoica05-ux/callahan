<?php

namespace Database\Factories;

use Illuminate\Database\Eloquent\Factories\Factory;

class MovieFactory extends Factory
{
    /**
     * Define the model's default state.
     */
    public function definition(): array
    {
        return [
            'tmdb_id' => fake()->numberBetween(-100000, 100000),
            'title' => fake()->sentence(4),
            'synopsis' => fake()->text(),
            'duration' => fake()->numberBetween(-10000, 10000),
            'year' => fake()->numberBetween(-10000, 10000),
            'poster_url' => fake()->word(),
            'last_synced_at' => fake()->dateTime(),
        ];
    }
}
