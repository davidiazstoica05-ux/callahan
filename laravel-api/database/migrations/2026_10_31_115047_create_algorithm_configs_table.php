<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('algorithm_configs', function (Blueprint $table) {
            $table->id();
            $table->float('adjustment_factor');
            $table->float('clamp_min');
            $table->float('clamp_max');
            $table->float('cutoff_score');
            $table->integer('list_size');
            $table->float('era_weight');
            $table->float('director_weight');
            $table->float('genre_weight');
            $table->float('duration_weight');
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('algorithm_configs');
    }
};
