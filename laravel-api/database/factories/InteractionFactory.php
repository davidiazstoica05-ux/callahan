<?php

namespace Database\Factories;

use App\Models\Movie;
use App\Models\User;
use Illuminate\Database\Eloquent\Factories\Factory;

class InteractionFactory extends Factory
{
    /**
     * Define the model's default state.
     */
    public function definition(): array
    {
        return [
            'user_id' => User::factory(),
            'movie_id' => Movie::factory(),
            'type' => fake()->regexify('[A-Za-z0-9]{20}'),
            'source' => fake()->regexify('[A-Za-z0-9]{20}'),
            'occurred_at' => fake()->dateTime(),
            'rating' => fake()->numberBetween(-8, 8),
        ];
    }
}
